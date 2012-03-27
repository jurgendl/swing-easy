package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

/**
 * @author Jurgen
 */
public class EDateChooser extends JPanel implements CyclingSpinnerListModelListener {
    /** serialVersionUID */
    private static final long serialVersionUID = 4865835185479753960L;

    protected static final String[] days = { "zo", "ma", "di", "wo", "do", "vr", "za" };

    /**
     * DateFormatSymbols returns an extra, empty value at the end of the array of months. Remove it.
     */
    public static String[] getMonthStrings() {
        String[] months = new java.text.DateFormatSymbols().getMonths();
        int lastIndex = months.length - 1;

        if ((months[lastIndex] == null) || (months[lastIndex].length() <= 0)) {
            // last item empty
            String[] monthStrings = new String[lastIndex];
            System.arraycopy(months, 0, monthStrings, 0, lastIndex);
            return monthStrings;
        }
        // last item not empty
        return months;
    }

    protected Calendar calendar;

    protected Date date;

    protected boolean cancelled;

    protected List<String> months = Arrays.asList(EDateChooser.getMonthStrings());

    protected JPanel contentPanel = new JPanel(new MigLayout("wrap 7", "[center]2px[center]2px[center]2px[center]2px[center]2px[center]2px[center]"));

    protected ESpinner<String> month;

    protected ESpinner<Integer> year;

    public EDateChooser() {
        this(null);
    }

    public EDateChooser(Date date) {
        this(date, Locale.getDefault());
    }

    public EDateChooser(Date date, Locale locale) {
        this(date, locale, TimeZone.getDefault());
    }

    public EDateChooser(Date date, Locale locale, TimeZone tz) {
        // System.out.println(" 1  2  3  4  5  6  7");
        // System.out.println("zo ma di wo do vr za");
        this.date = date;

        this.setLayout(new BorderLayout());

        JPanel tp = new JPanel(new MigLayout("", "[]rel[]10px[rel][]"));
        this.add(tp, BorderLayout.NORTH);

        CyclingSpinnerListModel<String> monthModel = new CyclingSpinnerListModel<String>(this.months);
        monthModel.addCyclingSpinnerListModelListener(this);
        this.month = new ESpinner<String>(monthModel);
        this.month.setMinimumSize(new Dimension(110, 1));
        tp.add(new JLabel(Messages.getString(this.getLocale(), "EDateChooser.month")), "");
        tp.add(this.month, "grow");

        List<Integer> years = new ArrayList<Integer>();
        for (int i = 1900; i <= 2100; i++) {
            years.add(i);
        }

        CyclingSpinnerListModel<Integer> yearModel = new CyclingSpinnerListModel<Integer>(years);
        this.year = new ESpinner<Integer>(yearModel);
        this.year.setMinimumSize(new Dimension(50, 1));
        tp.add(new JLabel(Messages.getString(this.getLocale(), "EDateChooser.year")), "");
        tp.add(this.year, "grow");

        this.add(this.contentPanel, BorderLayout.CENTER);

        this.calendar = new GregorianCalendar(tz, locale);

        if (date != null) {
            this.setDate(date);
        }

        this.month.setValue(this.months.get(this.calendar.get(Calendar.MONTH)));
        this.year.setValue(this.calendar.get(Calendar.YEAR));

        this.buildComponent();

        ChangeListener changelistener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Object m = EDateChooser.this.month.getValue();
                int mm = EDateChooser.this.months.indexOf(m);
                EDateChooser.this.changeMonth((Integer) EDateChooser.this.year.getValue(), mm, EDateChooser.this.calendar.get(Calendar.DAY_OF_MONTH));
            }
        };
        yearModel.addChangeListener(changelistener);
        monthModel.addChangeListener(changelistener);
    }

    protected void buildComponent() {
        // System.out.println(this.calendar.getTime());
        // System.out.println("* " + this.calendar.getFirstDayOfWeek());
        // System.out.println("Y " + this.calendar.get(Calendar.YEAR));
        // System.out.println("M " + this.calendar.get(Calendar.MONTH));
        // System.out.println("D " + this.calendar.get(Calendar.DAY_OF_MONTH));

        this.contentPanel.removeAll();

        int x = 0;

        for (int i = this.calendar.getFirstDayOfWeek(); i < (this.calendar.getFirstDayOfWeek() + 7); i++) {
            int j = i - 1;
            if (j >= EDateChooser.days.length) {
                j -= EDateChooser.days.length;
            }
            x++;
            this.contentPanel.add(new JLabel(EDateChooser.days[j]), "");
        }

        int dayOfMonth = this.calendar.get(Calendar.DAY_OF_MONTH);
        this.calendar.set(Calendar.DAY_OF_MONTH, 1);

        // System.out.println(this.calendar.getTime());
        // System.out.println("Y " + this.calendar.get(Calendar.YEAR));
        // System.out.println("M " + this.calendar.get(Calendar.MONTH));
        // System.out.println("D " + this.calendar.get(Calendar.DAY_OF_MONTH));
        // System.out.println("** " + this.calendar.get(Calendar.DAY_OF_WEEK));

        int empty = this.calendar.get(Calendar.DAY_OF_WEEK) - this.calendar.getFirstDayOfWeek();
        if (empty < 0) {
            empty += 7;
        }
        // System.out.println("empty " + empty);

        for (int i = 0; i < empty; i++) {
            x++;
            JLabel el = new JLabel();
            this.contentPanel.add(el, "");
        }

        ButtonGroup bg = new ButtonGroup();

        int maximum = this.calendar.getMaximum(Calendar.DAY_OF_MONTH);
        // System.out.println("max: " + maximum);

        Dimension defaultDimension = new Dimension(36, 20);
        int y = 0;
        for (int i = 0; i < maximum; i++) {
            final String id = String.valueOf((i + 1));
            EToggleButton comp = new EToggleButton(new EToolBarButtonCustomizer(defaultDimension), id);
            comp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EDateChooser.this.calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(id));
                }
            });
            comp.setMargin(new Insets(0, 0, 0, 0));
            comp.setOpaque(true);
            comp.setFont(this.getFont().deriveFont(8f));
            bg.add(comp);
            x++;
            if ((((x % 7) == 0) && (x != 0))) {
                y++;
                this.contentPanel.add(comp, "wrap");
            } else {
                this.contentPanel.add(comp, "");
            }

            if ((i + 1) == dayOfMonth) {
                comp.setSelected(true);
            }
        }
        // System.out.println("y " + y);
        if (y == 4) {
            for (int i = 0; i < 7; i++) {
                JLabel el = new JLabel();
                el.setMinimumSize(defaultDimension);
                this.contentPanel.add(el, "");
            }
        }

        this.contentPanel.revalidate();

        this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    protected void changeMonth(int _year, int _month, int _day) {
        this.calendar.set(Calendar.YEAR, _year);
        this.calendar.set(Calendar.MONTH, _month);
        // System.out.println(_day);
        int dom = Math.min(this.calendar.getMaximum(Calendar.DAY_OF_MONTH), _day);
        // System.out.println(">>" + dom);
        this.calendar.set(Calendar.DAY_OF_MONTH, dom);
        this.buildComponent();
    }

    public Date doShow() {
        this.cancelled = JOptionPane.showOptionDialog(null, this, Messages.getString(this.getLocale(), "EDateChooser.title"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == JOptionPane.CANCEL_OPTION;
        return this.getDate();
    }

    public Date getDate() {
        return this.cancelled ? this.date : this.calendar.getTime();
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * 
     * @see org.swingeasy.CyclingSpinnerListModelListener#overflow()
     */
    @Override
    public void overflow() {
        @SuppressWarnings("unchecked")
        CyclingSpinnerListModel<Integer> model = CyclingSpinnerListModel.class.cast(this.year.getModel());
        model.setValue(model.getNextValue());
    }

    /**
     * 
     * @see org.swingeasy.CyclingSpinnerListModelListener#rollback()
     */
    @Override
    public void rollback() {
        @SuppressWarnings("unchecked")
        CyclingSpinnerListModel<Integer> model = CyclingSpinnerListModel.class.cast(this.year.getModel());
        model.setValue(model.getPreviousValue());
    }

    public void setDate(Date date) {
        this.calendar.setTime(date);
    }
}
