package org.swingeasy.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.swingeasy.UIUtils;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.FreezableList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.TreeList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import ca.odell.glazedlists.swing.TreeTableSupport;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class LargeTreeTableTest {

    public static enum Continent {
        NorthAmerica, SouthAmerica, Europe, Africa, Asia, Anarctica, Australia
    }

    public static enum Country {
        USA, Germany, Australia, Canada, England
    }

    /**
     * Location is an example of a Bean with many disparate types represented by its properties: 2 enums, a String and an Integer.
     */
    public static final class Location {
        private final Continent continent;

        private final Country country;

        private final String city;

        private final Integer population;

        public Location(Continent continent, Country country, String city, Integer population) {
            this.continent = continent;
            this.country = country;
            this.city = city;
            this.population = population;
        }

        public String getCity() {
            return this.city;
        }

        public Continent getContinent() {
            return this.continent;
        }

        public Country getCountry() {
            return this.country;
        }

        public Integer getPopulation() {
            return this.population;
        }
    }

    private static class LocationTreeFormat implements TreeList.Format<LocationTreeNode> {
        @Override
        public boolean allowsChildren(LocationTreeNode element) {
            return true;
        }

        @Override
        public Comparator getComparator(int depth) {
            // at all levels of the tree, we can simply group nodes based on their hierarchy value
            return GlazedLists.beanPropertyComparator(LocationTreeNode.class, "hierarchyValue");
        }

        @Override
        public void getPath(List<LocationTreeNode> path, LocationTreeNode element) {
            // first level of the tree is continents
            path.add(new LocationTreeNode(null, element.getContinent().toString()));

            // second level of the tree is countries
            path.add(new LocationTreeNode(null, element.getCountry().toString()));

            // third level of the tree is the raw LocationTreeNodes themselves
            path.add(element);
        }
    }

    /**
     * This TreeNode combines a regular Location object with an explicit value to be used in the hierarchy column. For leaf nodes in the tree, the
     * Location object will be present and will match the corresponding element of the source node. For synthetic nodes that represent a parent node
     * in the tree, the Location object will be null and only the hierarchyValue will be present.
     */
    public static class LocationTreeNode {
        private final Location location;

        private final String hierarchyValue;

        public LocationTreeNode(Location location, String hierarchyValue) {
            this.location = location;
            this.hierarchyValue = hierarchyValue;
        }

        public String getCity() {
            return this.location == null ? null : this.location.getCity();
        }

        public Continent getContinent() {
            return this.location == null ? null : this.location.getContinent();
        }

        public Country getCountry() {
            return this.location == null ? null : this.location.getCountry();
        }

        public Comparable getHierarchyValue() {
            return this.hierarchyValue;
        }

        public Integer getPopulation() {
            return this.location == null ? null : this.location.getPopulation();
        }
    }

    public static JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // create some sample data
                final EventList<Location> locations = new BasicEventList<Location>(100000);

                for (int i = 0; i < 100; i++) {

                    locations.add(new Location(Continent.NorthAmerica, Country.USA, "Portland", 562690));
                    locations.add(new Location(Continent.Europe, Country.Germany, "Dresden", 504635));
                    locations.add(new Location(Continent.NorthAmerica, Country.USA, "San Jose", 929936));
                    locations.add(new Location(Continent.Australia, Country.Australia, "Brisbane", 958504));
                    locations.add(new Location(Continent.NorthAmerica, Country.Canada, "Regina", 179246));
                    locations.add(new Location(Continent.Australia, Country.Australia, "Perth", 1507900));
                    locations.add(new Location(Continent.NorthAmerica, Country.Canada, "Esterhazy", 2602));
                    locations.add(new Location(Continent.Australia, Country.Australia, "Sydney", 4119190));
                    locations.add(new Location(Continent.Europe, Country.England, "Brighton & Hove", 155919));
                }

                // transform the Location -> LocationTreeNode (which provides an independent "hierarchyValue")
                final FunctionList.Function<Location, LocationTreeNode> treeNodeFunction = new FunctionList.Function<Location, LocationTreeNode>() {
                    @Override
                    public LocationTreeNode evaluate(Location sourceValue) {
                        return new LocationTreeNode(sourceValue, String.valueOf(sourceValue.getCity()));
                    }
                };
                final EventList<LocationTreeNode> treeNodes = new FunctionList<Location, LocationTreeNode>(locations, treeNodeFunction);

                // transfer all ListEvents onto the EDT
                final EventList<LocationTreeNode> swingThreadProxyList = GlazedListsSwing.swingThreadProxyList(treeNodes);

                // SEARCH / FILTER -->>

                final JTextField filterEdit = new JTextField(25);

                // TextFilterator<CmsObjectWrapper> filterator = GlazedLists.textFilterator(new String[]{"hierarchyValue"});

                TextFilterator<LocationTreeNode> filterator = new TextFilterator<LocationTreeNode>() {

                    @Override
                    public void getFilterStrings(List<String> baseList, LocationTreeNode element) {

                        LocationTreeNode location = element;
                        baseList.add(location.getCity());
                        baseList.add(location.getContinent().toString());
                        baseList.add(location.getCountry().toString());

                    }
                };

                TextComponentMatcherEditor<LocationTreeNode> matcherEditor = new TextComponentMatcherEditor<LocationTreeNode>(filterEdit, filterator);

                // FilterList filteredLocations = new FilterList(swingThreadProxyList, new ThreadedMatcherEditor(matcherEditor));
                FilterList filteredLocations0 = new FilterList(swingThreadProxyList, matcherEditor);

                FreezableList<LocationTreeNode> filteredLocations = new FreezableList<LocationTreeNode>(filteredLocations0);

                LargeTreeTableTest.filterPanel.add(new JLabel("Filter:"));
                LargeTreeTableTest.filterPanel.add(filterEdit);

                // generate a tree from the base list of LocationTreeNode
                final TreeList<LocationTreeNode> locationTree = new TreeList<LocationTreeNode>(filteredLocations, new LocationTreeFormat(),
                        TreeList.NODES_START_EXPANDED);

                // built a flat table of LocationTreeNode
                final String[] labelNames = { "", "Continent", "Country", "City", "Population" };
                final String[] properties = { "hierarchyValue", "continent", "country", "city", "population" };
                final TableFormat<LocationTreeNode> locationTreeTableFormat = GlazedLists.tableFormat(properties, labelNames);
                final EventTableModel<LocationTreeNode> locationTreeTableModel = new EventTableModel<LocationTreeNode>(GlazedListsSwing
                        .swingThreadProxyList(locationTree), locationTreeTableFormat);
                final JTable locationTreeTable = new JTable(locationTreeTableModel);

                // now make the table a treetable
                TreeTableSupport.install(locationTreeTable, locationTree, 0);

                // show the treetable in a frame
                JFrame frame = new JFrame();

                frame.setLayout(new BorderLayout());

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.add(new JScrollPane(locationTreeTable), BorderLayout.CENTER);
                frame.add(LargeTreeTableTest.filterPanel, BorderLayout.NORTH);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}