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
public final class CreateOrderViewModel_Factory implements Factory<CreateOrderViewModel> {
  private final Provider<ServiceOrderRepository> serviceOrderRepositoryProvider;

  private final Provider<ClientRepository> clientRepositoryProvider;

  private final Provider<ServiceRepository> serviceRepositoryProvider;

  private final Provider<ProductRepository> productRepositoryProvider;

  public CreateOrderViewModel_Factory(
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider,
      Provider<ClientRepository> clientRepositoryProvider,
      Provider<ServiceRepository> serviceRepositoryProvider,
      Provider<ProductRepository> productRepositoryProvider) {
    this.serviceOrderRepositoryProvider = serviceOrderRepositoryProvider;
    this.clientRepositoryProvider = clientRepositoryProvider;
    this.serviceRepositoryProvider = serviceRepositoryProvider;
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public CreateOrderViewModel get() {
    return newInstance(serviceOrderRepositoryProvider.get(), clientRepositoryProvider.get(), serviceRepositoryProvider.get(), productRepositoryProvider.get());
  }

  public static CreateOrderViewModel_Factory create(
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider,
      Provider<ClientRepository> clientRepositoryProvider,
      Provider<ServiceRepository> serviceRepositoryProvider,
      Provider<ProductRepository> productRepositoryProvider) {
    return new CreateOrderViewModel_Factory(serviceOrderRepositoryProvider, clientRepositoryProvider, serviceRepositoryProvider, productRepositoryProvider);
  }

  public static CreateOrderViewModel newInstance(ServiceOrderRepository serviceOrderRepository,
      ClientRepository clientRepository, ServiceRepository serviceRepository,
      ProductRepository productRepository) {
    return new CreateOrderViewModel(serviceOrderRepository, clientRepository, serviceRepository, productRepository);
  }
}
