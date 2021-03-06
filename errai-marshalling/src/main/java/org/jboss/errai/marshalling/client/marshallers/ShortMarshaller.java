package org.jboss.errai.marshalling.client.marshallers;

import com.google.gwt.json.client.JSONValue;
import org.jboss.errai.marshalling.client.api.annotations.ClientMarshaller;
import org.jboss.errai.marshalling.client.api.Marshaller;
import org.jboss.errai.marshalling.client.api.MarshallingSession;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@ClientMarshaller
public class ShortMarshaller implements Marshaller<JSONValue, Short> {
  @Override
  public Class<Short> getTypeHandled() {
    return Short.class;
  }

  @Override
  public String getEncodingType() {
    return "json";
  }

  @Override
  public Short demarshall(JSONValue o, MarshallingSession ctx) {
    return new Double(o.isNumber().doubleValue()).shortValue();
  }

  @Override
  public String marshall(Short o, MarshallingSession ctx) {
    return o.toString();
  }

  @Override
  public boolean handles(JSONValue o) {
    return o.isNumber() != null;
  }
}
