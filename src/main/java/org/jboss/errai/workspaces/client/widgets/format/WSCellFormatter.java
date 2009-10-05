package org.jboss.errai.workspaces.client.widgets.format;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.workspaces.client.widgets.WSGrid;

//todo: this totally needs to be refactored... the formatter currently holds the value...
public abstract class WSCellFormatter {
    protected static WSGrid.WSCell wsCellReference;
    protected HTML html;
    protected boolean readonly = false;
    protected boolean cancel = false;

    public void cancelEdit() {
        cancel = true;
    }

    public void setValue(String value) {
        if (readonly) return;

        notifyCellUpdate(value);

        if (!cancel) {
            if (value == null || value.length() == 0) {
                html.setHTML("&nbsp;");
                return;
            }

            html.setHTML(value);
        } else
            cancel = false;
    }

    public String getTextValue() {
        return html.getHTML().equals("&nbsp;") ? "" : html.getHTML();
    }

    public Widget getWidget(WSGrid grid) {
        return html;
    }

    public abstract boolean edit(WSGrid.WSCell element);

    public abstract void stopedit();

    public void setHeight(String height) {
        html.setHeight(height);
    }

    public void setWidth(String width) {
        html.setWidth(width);
    }


    /**
     * Notify any registered listeners that the value is about to change.
     *
     * @param newValue
     */
    public void notifyCellUpdate(String newValue) {
        if (wsCellReference == null) return;

        wsCellReference.notifyCellUpdate(newValue);
    }
}