package com.rfpiscinas.serviceorder.ui.viewmodel;

import com.rfpiscinas.serviceorder.data.repository.ClientRepository;
import com.rfpiscinas.serviceorder.data.repository.ProductRepository;
import com.rfpiscinas.serviceorder.data.repository.ServiceOrderRepository;
import com.rfpiscinas.serviceorder.data.repository.ServiceRepository;
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
public final class AddServicesViewModel_Factory implements Factory<AddServicesViewModel> {
  private final Provider<ClientRepository> clientRepositoryProvider;

  private final Provider<ServiceRepository> serviceRepositoryProvider;

  private final Provider<ProductRepository> productRepositoryProvider;

  private final Provider<ServiceOrderRepository> serviceOrderRepositoryProvider;

  public AddServicesViewModel_Factory(Provider<ClientRepository> clientRepositoryProvider,
      Provider<ServiceRepository> serviceRepositoryProvider,
      Provider<ProductRepository> productRepositoryProvider,
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider) {
    this.clientRepositoryProvider = clientRepositoryProvider;
    this.serviceRepositoryProvider = serviceRepositoryProvider;
    this.productRepositoryProvider = productRepositoryProvider;
    this.serviceOrderRepositoryProvider = serviceOrderRepositoryProvider;
  }

  @Override
  public AddServicesViewModel get() {
    return newInstance(clientRepositoryProvider.get(), serviceRepositoryProvider.get(), productRepositoryProvider.get(), serviceOrderRepositoryProvider.get());
  }

  public static AddServicesViewModel_Factory create(
      Provider<ClientRepository> clientRepositoryProvider,
      Provider<ServiceRepository> serviceRepositoryProvider,
      Provider<ProductRepository> productRepositoryProvider,
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider) {
    return new AddServicesViewModel_Factory(clientRepositoryProvider, serviceRepositoryProvider, productRepositoryProvider, serviceOrderRepositoryProvider);
  }

  public static AddServicesViewModel newInstance(ClientRepository clientRepository,
      ServiceRepository serviceRepository, ProductRepository productRepository,
      ServiceOrderRepository serviceOrderRepository) {
    return new AddServicesViewModel(clientRepository, serviceRepository, productRepository, serviceOrderRepository);
  }
}
