package com.ikkerens.filecaptest.filecap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ikkerens.filecaptest.FilecapSendingApplication;
import com.ikkerens.filecaptest.FilecapUtil;
import com.ikkerens.filecaptest.model.File;
import com.ikkerens.filecaptest.model.FileMetaData;
import com.ikkerens.filecaptest.model.MultipartBuilder;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ikkerens.filecaptest.FilecapSendingApplication.FILECAP_HOSTNAME;

public class UploadFilesRequest {
    private String from;
    private String subject;
    private String comment;
    private String password;
    private boolean encryptMessage;
    private final List<String> recipients;
    private final File file;

    public UploadFilesRequest(@NotNull final File file) {
        Objects.requireNonNull(file);
        this.from = "system@centcom.network";
        this.subject = "File transfer";
        this.comment = "An encrypted file was sent to you!";
        this.password = null;
        this.encryptMessage = true;
        this.recipients = new ArrayList<>();
        this.file = file;
    }

    public void setFrom(@NotNull final String from) {
        Objects.requireNonNull(from);
        this.from = from;
    }

    public void setSubject(@NotNull final String subject) {
        Objects.requireNonNull(subject);
        this.subject = subject;
    }

    public void setComment(@NotNull final String comment) {
        Objects.requireNonNull(comment);
        this.comment = comment;
    }

    public void setPassword(@NotNull final String password) {
        Objects.requireNonNull(comment);
        this.password = password;
    }

    public void setEncryptMessage(boolean encryptMessage) {
        this.encryptMessage = encryptMessage;
    }

    public void addRecipient(@NotNull final String recipient) {
        Objects.requireNonNull(comment);
        this.recipients.add(recipient);
    }

    private static final String RECIPIENT_FORMAT = "rec%d";

    public HttpRequest build() throws JsonProcessingException {
        if (this.recipients.isEmpty())
            throw new IllegalStateException("Recipients may not be empty.");

        final MultipartBuilder builder = new MultipartBuilder();
        builder.addFormField("APIKey", FilecapSendingApplication.FILECAP_APIKEY);
        builder.addFormField("ids", FilecapUtil.generateFilecapID());
        builder.addFormField("from", this.from);
        builder.addFormField("subject", this.subject);
        builder.addFormField("comment", this.comment);
        if (this.password != null)
            builder.addFormField("password", this.password);
        builder.addFormField("encryptMessage", ((Boolean) this.encryptMessage).toString());
        for (int i = 0; i < this.recipients.size(); i++)
            builder.addFormField(String.format(RECIPIENT_FORMAT, i), this.recipients.get(i));

        final FileMetaData metaData = this.file.toMetaData();
        builder.addFormField("fileMetadata", metaData.toJSON());
        builder.addFilePart("file01", metaData.fileName(), metaData.mimeType(), this.file.getFileData());

        final HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(builder.build());

        final HttpRequest.Builder request = HttpRequest.newBuilder()
                .header("Content-Type", "multipart/form-data; boundary=" + builder.getBoundary())
                .POST(bodyPublisher);
        try {
            request.uri(new URI("https://" + FILECAP_HOSTNAME + "/FileCap/process_upload.jsp"));
        } catch (URISyntaxException e) {
            throw new RuntimeException("[Developer error] Malformed URL on Filecap endpoint");
        }

        return request.build();
    }
}
