package com.rfpiscinas.serviceorder.ui.viewmodel;

import com.rfpiscinas.serviceorder.data.repository.ServiceOrderRepository;
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
public final class ReportViewModel_Factory implements Factory<ReportViewModel> {
  private final Provider<ServiceOrderRepository> serviceOrderRepositoryProvider;

  public ReportViewModel_Factory(Provider<ServiceOrderRepository> serviceOrderRepositoryProvider) {
    this.serviceOrderRepositoryProvider = serviceOrderRepositoryProvider;
  }

  @Override
  public ReportViewModel get() {
    return newInstance(serviceOrderRepositoryProvider.get());
  }

  public static ReportViewModel_Factory create(
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider) {
    return new ReportViewModel_Factory(serviceOrderRepositoryProvider);
  }

  public static ReportViewModel newInstance(ServiceOrderRepository serviceOrderRepository) {
    return new ReportViewModel(serviceOrderRepository);
  }
}
