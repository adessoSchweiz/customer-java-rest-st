package ch.adesso.ti.customer;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import org.junit.Rule;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.airhacks.rulz.jaxrsclient.JAXRSClientProvider.buildWithURI;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CustomerIT {

    @Rule
    public JAXRSClientProvider provider = buildWithURI("http://customer-java-rest-myproject.127.0.0.1.nip.io/application/resources/customers");

    @Test
    public void crud() {
        JsonObjectBuilder todoBuilder = Json.createObjectBuilder();
        JsonObject todoToCreate = todoBuilder
                .add("name", "rob")
                .build();

        //create
        Response postResponse = this.provider.target()
                .request()
                .post(Entity.json(todoToCreate));
        assertThat(postResponse.getStatus(), is(201));
        String location = postResponse.getHeaderString("Location");
        System.out.println("location = " + location);

        //find
        JsonObject dedicatedTodo = this.provider.client()
                .target(location)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);
        assertThat(dedicatedTodo.keySet(), hasItem("name"));
        String name = dedicatedTodo.getJsonString("name").getString();
        assertThat(name, is("rob"));
    }

}
