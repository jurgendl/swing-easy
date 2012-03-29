package org.swingeasy;

import java.awt.Container;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;
import javax.swing.text.MaskFormatter;

import org.swingeasy.formatters.DateFormatBuilder;
import org.swingeasy.formatters.NumberFormatBuilder;
import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class FormattedFieldDemo {
    private static void addComponents(Container container) throws ParseException {
        container.setLayout(new GridLayout(-1, 2));

        final EButtonGroup localegroup = new EButtonGroup();
        JRadioButton en = new JRadioButton("en");//$NON-NLS-1$
        container.add(en);
        localegroup.add(en);
        JRadioButton nl = new JRadioButton("nl");//$NON-NLS-1$
        container.add(nl);
        localegroup.add(nl);
        localegroup.addPropertyChangeListener(EButtonGroup.SELECTION, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Locale l = new Locale(String.valueOf(evt.getNewValue()));
                SystemSettings.setCurrentLocale(l);
            }
        });

        container.add(new ELabel(""));
        container.add(new EFormattedTextField(new NumberFormatBuilder(NumberFormatBuilder.Type.Default), 1234.5678));
        container.add(new ELabel("currency"));
        container.add(new EFormattedTextField(new NumberFormatBuilder(NumberFormatBuilder.Type.Currency), 1234.5678));
        container.add(new ELabel("integer"));
        container.add(new EFormattedTextField(new NumberFormatBuilder(NumberFormatBuilder.Type.Integer), 1234));
        container.add(new ELabel("number"));
        container.add(new EFormattedTextField(new NumberFormatBuilder(NumberFormatBuilder.Type.Number), 1234.5678));
        container.add(new ELabel("percent"));
        container.add(new EFormattedTextField(new NumberFormatBuilder(NumberFormatBuilder.Type.Percentage), 0.12345678));
        container.add(new ELabel("date"));
        container.add(new EFormattedTextField(new DateFormatBuilder(DateFormatBuilder.Type.Date, DateFormatBuilder.Length.Default), new Date()));
        container.add(new ELabel("time"));
        container.add(new EFormattedTextField(new DateFormatBuilder(DateFormatBuilder.Type.Time, DateFormatBuilder.Length.Default), new Date()));
        container.add(new ELabel("date/time"));
        container.add(new EFormattedTextField(new DateFormatBuilder(DateFormatBuilder.Type.Both, DateFormatBuilder.Length.Default), new Date()));
        container.add(new ELabel("mask"));
        container.add(new EFormattedTextField(new MaskFormatter("(###) ###-###"), "(032) 111-222"));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        try {
            FormattedFieldDemo.addComponents(frame.getContentPane());
        } catch (ParseException ex) {
            //
        }
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle("Demo");
        frame.setVisible(true);
    }
}
