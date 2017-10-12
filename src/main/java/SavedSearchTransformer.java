import Savedsearch_proto.Savedsearch;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

public class SavedSearchTransformer extends ResponseTransformer {
    @Override
    public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
        String method = request.getMethod().toString();
        String url = request.getUrl();

        System.out.println(request.toString());

        try {
            if (method.equals("GET") && url.equals("/ss/3.1/searches/")) {
                System.out.println("GetSavedSearchResponse...");
                Savedsearch.GetSavedSearchResponse.Builder builder = Savedsearch.GetSavedSearchResponse.newBuilder();
                JsonFormat.parser().merge(response.getBodyAsString(), builder);
                return Response.Builder.like(response).but().body(builder.build().toByteArray()).build();
            }
            else if (method.equals("POST") && url.equals("/ss/3.1/searches/")) {
                System.out.println("AddSavedSearchResponse...");
                Savedsearch.DefaultResponse.Builder builder = Savedsearch.DefaultResponse.newBuilder();
                JsonFormat.parser().merge(response.getBodyAsString(), builder);
                return Response.Builder.like(response).but().body(builder.build().toByteArray()).build();
            }
            else if (method.equals("PUT") && url.matches("/ss/3.1/searches/.*/")) {
                System.out.println("UpdateSavedSearchResponse...");
                Savedsearch.DefaultResponse.Builder builder = Savedsearch.DefaultResponse.newBuilder();
                JsonFormat.parser().merge(response.getBodyAsString(), builder);
                return Response.Builder.like(response).but().body(builder.build().toByteArray()).build();
            }
            else if (method.equals("DELETE") && url.matches("/ss/3.1/searches/.*/")) {
                System.out.println("DeleteSavedSearchResponse...");
                Savedsearch.DefaultResponse.Builder builder = Savedsearch.DefaultResponse.newBuilder();
                JsonFormat.parser().merge(response.getBodyAsString(), builder);
                return Response.Builder.like(response).but().body(builder.build().toByteArray()).build();
            }
            else return response;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return response;
        }
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
