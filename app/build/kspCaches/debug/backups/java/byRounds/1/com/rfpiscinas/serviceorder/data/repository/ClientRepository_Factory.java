package com.rfpiscinas.serviceorder.data.repository;

import com.rfpiscinas.serviceorder.data.local.dao.ClientDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ClientRepository_Factory implements Factory<ClientRepository> {
  private final Provider<ClientDao> clientDaoProvider;

  public ClientRepository_Factory(Provider<ClientDao> clientDaoProvider) {
    this.clientDaoProvider = clientDaoProvider;
  }

  @Override
  public ClientRepository get() {
    return newInstance(clientDaoProvider.get());
  }

  public static ClientRepository_Factory create(Provider<ClientDao> clientDaoProvider) {
    return new ClientRepository_Factory(clientDaoProvider);
  }

  public static ClientRepository newInstance(ClientDao clientDao) {
    return new ClientRepository(clientDao);
  }
}
