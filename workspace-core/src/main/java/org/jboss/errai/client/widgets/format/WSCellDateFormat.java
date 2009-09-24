package org.jboss.errai.client.widgets.format;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import org.jboss.errai.client.widgets.WSGrid;

import static java.lang.String.valueOf;
import java.util.Date;


public class WSCellDateFormat extends WSCellFormatter {
    private Date date;
    private String formatPattern = "MMM dd, yyyy";

    private static DatePicker datePicker;
    private static WSCellDateFormat editCellReference;

    static {
        datePicker = new DatePicker();
        RootPanel.get().add(datePicker);
        datePicker.getElement().getStyle().setProperty("position", "absolute");
        datePicker.setVisible(false);

        datePicker.addValueChangeHandler(new ValueChangeHandler() {
            public void onValueChange(ValueChangeEvent valueChangeEvent) {
                wsCellReference.setValue(valueOf(((Date) valueChangeEvent.getValue()).getTime()));
                datePicker.setVisible(false);
                editCellReference.stopedit();
            }
        });
        
    }

    public WSCellDateFormat(String value) {
        this.html = new HTML(value);

        setValue(value);
    }

    public WSCellDateFormat(Date date) {
        this.date = date;
        this.html = new HTML();
        setValue(date);
    }

    public String getFormatPattern() {
        return formatPattern;
    }

    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    public void setValue(String value) {
        if (value == null || value.length() == 0) {
            return;
        }

        setValue(new Date(Long.parseLong(value)));
    }

    public void setValue(Date date) {
        notifyCellUpdate(valueOf(date.getTime()));
        this.date = date;
        html.setHTML(DateTimeFormat.getFormat(formatPattern).format(date));
    }

    @Override
    public String getTextValue() {
        return valueOf(date.getTime());
    }


    public boolean edit(WSGrid.WSCell element) {
        wsCellReference = element;
        editCellReference = this;

        datePicker.setValue(date);
        datePicker.setCurrentMonth(date);

        Style s = datePicker.getElement().getStyle();

        int left = (element.getAbsoluteLeft() + element.getOffsetWidth() - 20);

        if ((left + datePicker.getOffsetWidth()) > Window.getClientHeight()) {
             left = Window.getClientHeight() - datePicker.getOffsetHeight();
        }

        s.setProperty("left", left + "px");
        s.setProperty("top", (element.getAbsoluteTop() + element.getOffsetHeight()) + "px");

        datePicker.setVisible(true);
        return true;
    }

    public void stopedit() {
        datePicker.setVisible(false);
        wsCellReference.stopedit();
    }
}