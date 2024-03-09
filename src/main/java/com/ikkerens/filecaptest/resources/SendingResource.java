package com.ikkerens.filecaptest.resources;

import com.ikkerens.filecaptest.filecap.UploadFilesRequest;
import com.ikkerens.filecaptest.filecap.UploadFilesResponse;
import com.ikkerens.filecaptest.model.File;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Path("/send")
public class SendingResource {
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain")
    public String sendFile(
            @FormParam("email") String target,
            @FormParam("subject") String subject,
            @FormParam("comment") String comment,
            @FormParam("password") String password,
            @FormDataParam("file") InputStream fileStream,
            @FormDataParam("file") FormDataContentDisposition fileInfo
    ) {
        try {
            // Arrange the client, the file and the request we're about to send
            final HttpClient client = HttpClient.newHttpClient();
            final File toSend = File.fromRequest(fileInfo.getFileName(), fileStream);
            final UploadFilesRequest request = new UploadFilesRequest(toSend);

            // Add parameters as requested by the client
            request.addRecipient(target);
            if (subject != null && !subject.isBlank())
                request.setSubject(subject);
            if (comment != null && !comment.isBlank())
                request.setComment(comment);
            if (password != null && !password.isBlank())
                request.setPassword(password);

            // Send request, parse the response
            final HttpResponse<String> httpResponse = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
            final UploadFilesResponse response = UploadFilesResponse.parseResponse(httpResponse.body());

            // Handle the response
            if (response.isSuccess())
                return "File was successfully sent.";
            else if (!response.hasError())
                return "File failed to send, but did not contain an error.";
            else
                return "File failed to send, yielding error: " + response.getError();
        } catch (Exception e) {
            throw new WebApplicationException("Internal server error occurred", e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
