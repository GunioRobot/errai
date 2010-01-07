/*
 * Copyright 2009 JBoss, a divison Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.errai.samples.queryservice.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import org.jboss.errai.bus.client.*;

public class QueryWidget extends Composite {
    @UiHandler("sendQuery")
    void doSubmit(ClickEvent event) {

        MessageBuilder.createCall()
                .call("QueryService")
                .endpoint("getQuery", queryBox.getText())
                .respondTo(String[].class, new RemoteCallback<String[]>() {
                    public void callback(String[] resultsString) {
                        if (resultsString == null) {
                            resultsString = new String[]{"No results."};
                        }

                        /**
                         * Build an HTML unordered list based on the results.
                         */
                        StringBuffer buf = new StringBuffer("<ul>");
                        for (String result : resultsString) {
                            buf.append("<li>").append(result).append("</li>");
                        }
                        results.setHTML(buf.append("</ul>").toString());
                    }
                })
                .noErrorHandling()
                .sendNowWith(bus);
    }

    /**
     * Do boilerplate for UIBinder
     */
    @UiTemplate("QueryWidget.ui.xml")
    interface Binder extends UiBinder<Panel, QueryWidget> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    {
        initWidget(binder.createAndBindUi(this));
    }

    @UiField
    TextBox queryBox;

    @UiField
    HTML results;

    private MessageBus bus = ErraiBus.get();
}



