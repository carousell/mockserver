import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.Router;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.AdminApiExtension;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource;
import com.github.tomakehurst.wiremock.standalone.MappingsSource;
import com.google.protobuf.InvalidProtocolBufferException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class CarousellHttpMockServer implements AdminApiExtension, AdminTask {

    WireMockServer wireMockServer;

    CarousellHttpMockServer() {
        WireMockConfiguration config = wireMockConfig()
                .port(8089)
                .notifier(new ConsoleNotifier(true))
                .usingFilesUnderDirectory("./")
                .extensions(this, new SavedSearchTransformer());
        this.wireMockServer = new WireMockServer(config);
    }

    public static void main(String [] args) throws InvalidProtocolBufferException {
        CarousellHttpMockServer carousellHttpMockServer = new CarousellHttpMockServer();
        carousellHttpMockServer.wireMockServer.start();
    }

    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        String tid = pathParams.get("id");
        FileSource fileSource = this.wireMockServer.getOptions().filesRoot().child("__tests").child(tid);
        
        this.wireMockServer.resetAll();

        if (fileSource.exists() == false) {
            return ResponseDefinition.notConfigured();
        }

        MappingsSource mappingsSource = new JsonFileMappingsSource(fileSource);
        this.wireMockServer.loadMappingsUsing(mappingsSource);

        return ResponseDefinition.ok();
    }

    @Override
    public void contributeAdminApiRoutes(Router router) {
        router.add(RequestMethod.GET, "/tid/{id}", this);
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
