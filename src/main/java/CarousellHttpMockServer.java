import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.google.protobuf.InvalidProtocolBufferException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class CarousellHttpMockServer {
    public static void main(String [] args) throws InvalidProtocolBufferException {
        WireMockConfiguration config = wireMockConfig().port(8089).extensions(new SavedSearchTransformer());
        WireMockServer wireMockServer = new WireMockServer(config);
        wireMockServer.start();
    }
}
