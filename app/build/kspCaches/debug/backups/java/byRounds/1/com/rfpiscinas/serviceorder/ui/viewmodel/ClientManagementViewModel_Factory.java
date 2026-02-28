package com.rfpiscinas.serviceorder.ui.viewmodel;

import com.rfpiscinas.serviceorder.data.repository.ClientRepository;
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
public final class ClientManagementViewModel_Factory implements Factory<ClientManagementViewModel> {
  private final Provider<ClientRepository> clientRepositoryProvider;

  public ClientManagementViewModel_Factory(Provider<ClientRepository> clientRepositoryProvider) {
    this.clientRepositoryProvider = clientRepositoryProvider;
  }

  @Override
  public ClientManagementViewModel get() {
    return newInstance(clientRepositoryProvider.get());
  }

  public static ClientManagementViewModel_Factory create(
      Provider<ClientRepository> clientRepositoryProvider) {
    return new ClientManagementViewModel_Factory(clientRepositoryProvider);
  }

  public static ClientManagementViewModel newInstance(ClientRepository clientRepository) {
    return new ClientManagementViewModel(clientRepository);
  }
}
