package org.embulk.input.google_spreadsheets_java;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ComputeEngineCredentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.auth.oauth2.UserCredentials;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.embulk.config.ConfigException;
import org.embulk.util.config.units.LocalFile;

public class Auth {
  private static final String SCOPE = SheetsScopes.SPREADSHEETS_READONLY;
  // https://cloud.google.com/docs/authentication/application-default-credentials
  private static final String APPLICATION_DEFAULT_CREDENTIALS =
      "~/.config/gcloud/application_default_credentials.json";
  private static final String GLOBAL_APPLICATION_DEFAULT_CREDENTIALS =
      "/etc/google/auth/application_default_credentials.json";
  private final String authMethod;
  private final LocalFile jsonKeyFile;

  public Auth(PluginTask task) {
    authMethod = task.getAuthMethod();
    jsonKeyFile = task.getJsonKeyfile().orElse(null);
  }

  public HttpRequestInitializer getHttpRequestInitializer() throws IOException {
    GoogleCredentials credentials = getGoogleCredentials().createScoped(SCOPE);
    credentials.refreshIfExpired();
    return new HttpCredentialsAdapter(credentials);
  }

  private GoogleCredentials getGoogleCredentials() throws IOException {
    if ("authorized_user".equalsIgnoreCase(authMethod)) {
      return UserCredentials.fromStream(getCredentialsStream());
    } else if ("service_account".equalsIgnoreCase(authMethod)) {
      return ServiceAccountCredentials.fromStream(getCredentialsStream());
    } else if ("compute_engine".equalsIgnoreCase(authMethod)) {
      return ComputeEngineCredentials.create();
    } else if ("application_default".equalsIgnoreCase(authMethod)) {
      return GoogleCredentials.getApplicationDefault();
    } else {
      throw new ConfigException("Unknown auth method: " + authMethod);
    }
  }

  private InputStream getCredentialsStream() throws IOException {
    return jsonKeyFile != null
        ? new ByteArrayInputStream(jsonKeyFile.getContent())
        : Files.newInputStream(getCredentialsFile().toPath());
  }

  private static File getCredentialsFile() {
    File file = new File(GLOBAL_APPLICATION_DEFAULT_CREDENTIALS);
    return file.exists()
        ? file
        : new File(
            APPLICATION_DEFAULT_CREDENTIALS.replaceFirst("^~", System.getProperty("user.home")));
  }
}
