package com.rfpiscinas.serviceorder.ui.viewmodel;

import com.rfpiscinas.serviceorder.data.repository.UserRepository;
import com.rfpiscinas.serviceorder.util.SessionManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class LoginViewModel_Factory implements Factory<LoginViewModel> {
  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public LoginViewModel_Factory(Provider<UserRepository> userRepositoryProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public LoginViewModel get() {
    return newInstance(userRepositoryProvider.get(), sessionManagerProvider.get());
  }

  public static LoginViewModel_Factory create(Provider<UserRepository> userRepositoryProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new LoginViewModel_Factory(userRepositoryProvider, sessionManagerProvider);
  }

  public static LoginViewModel newInstance(UserRepository userRepository,
      SessionManager sessionManager) {
    return new LoginViewModel(userRepository, sessionManager);
  }
}
