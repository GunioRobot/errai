package org.jboss.workspace.client.widgets;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;


public class WSGrid extends Composite {
    private WSAbstractGrid titleBar;
    private WSAbstractGrid dataGrid;

    private ArrayList<Integer> columnWidths;

    private int cols;

    private WSCell currentFocus;

    private ArrayList<ArrayList<WSCell>> tableIndex;

    public WSGrid() {
        this(true);
    }

    public WSGrid(boolean scrollable) {
        VerticalPanel panel = new VerticalPanel();

        panel.setWidth("100%");

        panel.add(titleBar = new WSAbstractGrid(false));
        titleBar.setStylePrimaryName("WSGrid-header");
        panel.setCellHeight(titleBar, titleBar.getOffsetHeight() + "px");

        panel.add(dataGrid = new WSAbstractGrid(scrollable));
        panel.setCellVerticalAlignment(dataGrid, HasVerticalAlignment.ALIGN_TOP);

        dataGrid.setStylePrimaryName("WSGrid-datagrid");

        columnWidths = new ArrayList<Integer>();
        tableIndex = new ArrayList<ArrayList<WSCell>>();
        tableIndex.add(new ArrayList<WSCell>());

        initWidget(panel);

        DOM.addEventPreview(new EventPreview() {
            public boolean onEventPreview(Event event) {
                switch (event.getTypeInt()) {
                    case Event.ONKEYPRESS:
                        if (currentFocus == null || currentFocus.edit) return true;

                        switch (event.getKeyCode()) {
                            case KeyboardListener.KEY_TAB:
                                if (event.getShiftKey()) {
                                    if (currentFocus.getCol() == 0 && currentFocus.getRow() > 0) {
                                        tableIndex.get(currentFocus.getRow() - 1).get(cols - 1).focus();
                                    }
                                    else {
                                        tableIndex.get(currentFocus.getRow()).get(currentFocus.getCol() - 1).focus();
                                    }
                                }
                                else {
                                    if (currentFocus.getCol() == cols - 1 && currentFocus.getRow() < tableIndex.size()) {
                                        tableIndex.get(currentFocus.getRow() + 1).get(0).focus();
                                    }
                                    else {
                                        tableIndex.get(currentFocus.getRow()).get(currentFocus.getCol() + 1).focus();
                                    }
                                }
                                break;

                            case 63232:
                            case KeyboardListener.KEY_UP:
                                if (currentFocus.getRow() > 0)
                                    tableIndex.get(currentFocus.getRow() - 1).get(currentFocus.getCol()).focus();
                                break;
                            case 63235:
                            case KeyboardListener.KEY_RIGHT:
                                if (currentFocus.getCol() < cols)
                                    tableIndex.get(currentFocus.getRow()).get(currentFocus.getCol() + 1).focus();
                                break;
                            case 63233:
                            case KeyboardListener.KEY_ENTER:
                            case KeyboardListener.KEY_DOWN:
                                if (currentFocus.getRow() < tableIndex.size())
                                    tableIndex.get(currentFocus.getRow() + 1).get(currentFocus.getCol()).focus();
                                break;
                            case 63234:
                            case KeyboardListener.KEY_LEFT:
                                if (currentFocus.getCol() > 0)
                                    tableIndex.get(currentFocus.getRow()).get(currentFocus.getCol() - 1).focus();
                                break;

                            case 63272:
                            case KeyboardListener.KEY_DELETE:
                                currentFocus.getWrappedWidget().setHTML("");
                                break;

                            case 32: // spacebar
                                currentFocus.edit();
                                break;

                        }


                }


                return true;
            }
        });

    }


    public void setColumnHeader(int row, int column, String html) {
        cols = titleBar.ensureRowsAndCols(row+1, column+1);

        System.out.println("set header:" + row + "; col:" + column);

        tableIndex.get(row).get(column).getWrappedWidget().setHTML(html);

//        Widget newWidget = new WSCell(new HTML(html), row, column);
//        newWidget.setWidth(checkWidth(column) + "px");
//
//        titleBar.getTable().setWidget(row, column, newWidget);
    }


    public void setCell(int row, int column, String html) {
        cols = dataGrid.ensureRowsAndCols(row+1, column);

        System.out.println("set row:" + row + "; col:" + column);

        tableIndex.get(row).get(column).getWrappedWidget().setHTML(html);

       // Widget newWidget = new WSCell(new HTML(html), row, column);
       // newWidget.setWidth(checkWidth(column) + "px");

       // dataGrid.getTable().setWidget(row, column, newWidget);
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getCols() {
        return cols;
    }

    private int checkWidth(int column) {
        if (columnWidths.size() - 1 < column) {
            for (int i = 0; i <= column; i++) {
                columnWidths.add(150);
            }
        }

        return columnWidths.get(column);
    }

    public void updateWidth(int column, int width) {
        HTMLTable.ColumnFormatter colFormatter = titleBar.getTable().getColumnFormatter();
        colFormatter.setWidth(column, width + "px");

        colFormatter = dataGrid.getTable().getColumnFormatter();
        colFormatter.setWidth(column, width + "px");

        checkWidth(column);
        columnWidths.set(column, width);
    }


    public class WSAbstractGrid extends Composite {
        private ScrollPanel scrollPanel;
        private FlexTable table;

        public WSAbstractGrid() {
            this(false);
        }

        public WSAbstractGrid(boolean scrollable) {
            table = new FlexTable();

            table.setStylePrimaryName("WSGrid");

            table.insertRow(0);

            if (scrollable) {
                scrollPanel = new ScrollPanel(table);
                initWidget(scrollPanel);
            }
            else {
                initWidget(table);
            }

        }

        public void addCell(int row, HTML w) {
            if (row > table.getRowCount()) {
                for (int i = table.getRowCount(); i <= row; i++) {
                    table.insertRow(i);
                }
            }

            int col = table.getCellCount(row);
            table.addCell(row);

            table.setWidget(row, col , new WSCell(w, row, col));
        }

        public void addRow() {
            table.insertRow(table.getRowCount());
            for (int i = 0; i < cols; i++) {
                addCell(table.getRowCount()-1, new HTML());
            }

        }

        public int ensureRowsAndCols(int rows, int cols) {
            System.out.println("ensure rows=" + rows + "; cols=" + cols);
            FlexTable flexTable = dataGrid.getTable();
            if (flexTable.getRowCount() == 0) {
                addRow();
            }

            while (flexTable.getRowCount() < rows) {
                addRow();
            }

            if (flexTable.getCellCount(0) < cols) {
                int newCols = cols - flexTable.getCellCount(0);

                for (int i = 0; i < flexTable.getRowCount(); i++) {
                    for (int x = 0; x < newCols; x++) {
                        addCell(i, new HTML());
                    }
                }

                return cols;
            }
            else if (cols == 0) {
                addCell(0, new HTML());
                return 1;
            }
            else {
                return flexTable.getCellCount(0);
            }
        }

        public FlexTable getTable() {
            return table;
        }
    }

    public class WSCell extends Composite {
        private SimplePanel panel;
        private HTML wrappedWidget;
        private boolean edit;
        private TextBox textBox;

        private int row;
        private int col;

        public WSCell(HTML widget, int row, int col) {
            panel = new SimplePanel();
            textBox = new TextBox();
            textBox.setStylePrimaryName("WSCell-editbox");

            textBox.addFocusListener(new FocusListener() {
                public void onFocus(Widget sender) {
                }

                public void onLostFocus(Widget sender) {
                   stopedit(); 
                }
            });

            if (tableIndex.size() - 1 < row) {
                while (tableIndex.size() - 1 < row) {
                    tableIndex.add(new ArrayList<WSCell>());
                }
            }
            ArrayList<WSCell> cols = tableIndex.get(row);

            if (cols.size() == 0 || cols.size() - 1 < col) {
                cols.add(this);
            }
            else {
                cols.set(col, this);
            }


            this.wrappedWidget = widget;
            panel.add(wrappedWidget);

            this.row = row;
            this.col = col;

            initWidget(panel);
            setStyleName("WSCell");
            sinkEvents(Event.MOUSEEVENTS | Event.FOCUSEVENTS | Event.ONCLICK | Event.ONDBLCLICK);
        }

        public void edit() {
            panel.remove(wrappedWidget);

            textBox.setWidth(getOffsetWidth() + "px");
            textBox.setText(wrappedWidget.getHTML());
            panel.add(textBox);

            edit = true;

            textBox.selectAll();
            textBox.setFocus(true);
        }

        public void stopedit() {
                      if (edit) {
                wrappedWidget.setHTML(textBox.getText());
                panel.remove(textBox);
                panel.add(wrappedWidget);

                edit = false;
            }
        }

        public void blur() {
            stopedit();

            removeStyleDependentName("selected");
        }

        public void focus() {
            if (currentFocus != null && currentFocus != this) {
                currentFocus.blur();
            }
            currentFocus = this;

            addStyleDependentName("selected");
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public HTML getWrappedWidget() {
            return wrappedWidget;
        }

        @Override
        public void onBrowserEvent(Event event) {
            switch (event.getTypeInt()) {
                case Event.ONMOUSEOVER:
                    addStyleDependentName("hover");
                    break;
                case Event.ONMOUSEOUT:
                    removeStyleDependentName("hover");
                    break;

                case Event.ONFOCUS:
                    break;

                case Event.ONCLICK:
                    break;

                case Event.ONDBLCLICK:
                    edit();
                    break;

                case Event.ONMOUSEUP:
                    focus();

                    break;
            }
        }
    }
}