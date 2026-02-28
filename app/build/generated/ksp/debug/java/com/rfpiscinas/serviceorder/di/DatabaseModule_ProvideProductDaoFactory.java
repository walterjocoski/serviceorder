package com.rfpiscinas.serviceorder.di;

import com.rfpiscinas.serviceorder.data.local.AppDatabase;
import com.rfpiscinas.serviceorder.data.local.dao.ProductDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideProductDaoFactory implements Factory<ProductDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideProductDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ProductDao get() {
    return provideProductDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideProductDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideProductDaoFactory(dbProvider);
  }

  public static ProductDao provideProductDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideProductDao(db));
  }
}
