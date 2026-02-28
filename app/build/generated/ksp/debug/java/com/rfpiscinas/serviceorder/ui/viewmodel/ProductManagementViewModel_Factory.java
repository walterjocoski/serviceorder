package com.rfpiscinas.serviceorder.ui.viewmodel;

import com.rfpiscinas.serviceorder.data.repository.ProductRepository;
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
public final class ProductManagementViewModel_Factory implements Factory<ProductManagementViewModel> {
  private final Provider<ProductRepository> productRepositoryProvider;

  public ProductManagementViewModel_Factory(Provider<ProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public ProductManagementViewModel get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static ProductManagementViewModel_Factory create(
      Provider<ProductRepository> productRepositoryProvider) {
    return new ProductManagementViewModel_Factory(productRepositoryProvider);
  }

  public static ProductManagementViewModel newInstance(ProductRepository productRepository) {
    return new ProductManagementViewModel(productRepository);
  }
}
