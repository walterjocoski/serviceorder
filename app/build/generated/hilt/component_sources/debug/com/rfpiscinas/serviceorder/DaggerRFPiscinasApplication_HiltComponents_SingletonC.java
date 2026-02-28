package com.rfpiscinas.serviceorder;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.rfpiscinas.serviceorder.data.local.AppDatabase;
import com.rfpiscinas.serviceorder.data.local.dao.ClientDao;
import com.rfpiscinas.serviceorder.data.local.dao.ProductDao;
import com.rfpiscinas.serviceorder.data.local.dao.ServiceDao;
import com.rfpiscinas.serviceorder.data.local.dao.ServiceOrderDao;
import com.rfpiscinas.serviceorder.data.local.dao.UserDao;
import com.rfpiscinas.serviceorder.data.repository.ClientRepository;
import com.rfpiscinas.serviceorder.data.repository.ProductRepository;
import com.rfpiscinas.serviceorder.data.repository.ServiceOrderRepository;
import com.rfpiscinas.serviceorder.data.repository.ServiceRepository;
import com.rfpiscinas.serviceorder.data.repository.UserRepository;
import com.rfpiscinas.serviceorder.di.DatabaseModule_ProvideClientDaoFactory;
import com.rfpiscinas.serviceorder.di.DatabaseModule_ProvideDatabaseFactory;
import com.rfpiscinas.serviceorder.di.DatabaseModule_ProvideProductDaoFactory;
import com.rfpiscinas.serviceorder.di.DatabaseModule_ProvideServiceDaoFactory;
import com.rfpiscinas.serviceorder.di.DatabaseModule_ProvideServiceOrderDaoFactory;
import com.rfpiscinas.serviceorder.di.DatabaseModule_ProvideUserDaoFactory;
import com.rfpiscinas.serviceorder.ui.screens.employee.OrderDetailViewModel;
import com.rfpiscinas.serviceorder.ui.screens.employee.OrderDetailViewModel_HiltModules_KeyModule_ProvideFactory;
import com.rfpiscinas.serviceorder.ui.viewmodel.AddServicesViewModel;
import com.rfpiscinas.serviceorder.ui.viewmodel.AddServicesViewModel_HiltModules_KeyModule_ProvideFactory;
import com.rfpiscinas.serviceorder.ui.viewmodel.AllOrdersViewModel;
import com.rfpiscinas.serviceorder.ui.viewmodel.AllOrdersViewModel_HiltModules_KeyModule_ProvideFactory;
import com.rfpiscinas.serviceorder.ui.viewmodel.ClientManagementViewModel;
import com.rfpiscinas.serviceorder.ui.viewmodel.ClientManagementViewModel_HiltModules_KeyModule_ProvideFactory;
import com.rfpiscinas.serviceorder.ui.viewmodel.CreateOrderViewModel;
import com.rfpiscinas.serviceorder.ui.viewmodel.CreateOrderViewModel_HiltModules_KeyModule_ProvideFactory;
import com.rfpiscinas.serviceorder.ui.viewmodel.EmployeeHomeViewModel;
import com.rfpiscinas.serviceorder.ui.viewmodel.EmployeeHomeViewModel_HiltModules_KeyModule_ProvideFactory;
import com.rfpiscinas.serviceorder.ui.viewmodel.EmployeeManagementViewModel;
import com.rfpiscinas.serviceorder.ui.viewmodel.EmployeeManagementViewModel_HiltModules_KeyModule_ProvideFactory;
import com.rfpiscinas.serviceorder.ui.viewmodel.LoginViewModel;
import com.rfpiscinas.serviceorder.ui.viewmodel.LoginViewModel_HiltModules_KeyModule_ProvideFactory;
import com.rfpiscinas.serviceorder.ui.viewmodel.ProductManagementViewModel;
import com.rfpiscinas.serviceorder.ui.viewmodel.ProductManagementViewModel_HiltModules_KeyModule_ProvideFactory;
import com.rfpiscinas.serviceorder.ui.viewmodel.ReportViewModel;
import com.rfpiscinas.serviceorder.ui.viewmodel.ReportViewModel_HiltModules_KeyModule_ProvideFactory;
import com.rfpiscinas.serviceorder.ui.viewmodel.ServiceManagementViewModel;
import com.rfpiscinas.serviceorder.ui.viewmodel.ServiceManagementViewModel_HiltModules_KeyModule_ProvideFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SetBuilder;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerRFPiscinasApplication_HiltComponents_SingletonC {
  private DaggerRFPiscinasApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public RFPiscinasApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements RFPiscinasApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public RFPiscinasApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements RFPiscinasApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public RFPiscinasApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements RFPiscinasApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public RFPiscinasApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements RFPiscinasApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public RFPiscinasApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements RFPiscinasApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public RFPiscinasApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements RFPiscinasApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public RFPiscinasApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements RFPiscinasApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public RFPiscinasApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends RFPiscinasApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends RFPiscinasApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends RFPiscinasApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends RFPiscinasApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return SetBuilder.<String>newSetBuilder(11).add(AddServicesViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(AllOrdersViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(ClientManagementViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(CreateOrderViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(EmployeeHomeViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(EmployeeManagementViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(LoginViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(OrderDetailViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(ProductManagementViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(ReportViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(ServiceManagementViewModel_HiltModules_KeyModule_ProvideFactory.provide()).build();
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends RFPiscinasApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AddServicesViewModel> addServicesViewModelProvider;

    private Provider<AllOrdersViewModel> allOrdersViewModelProvider;

    private Provider<ClientManagementViewModel> clientManagementViewModelProvider;

    private Provider<CreateOrderViewModel> createOrderViewModelProvider;

    private Provider<EmployeeHomeViewModel> employeeHomeViewModelProvider;

    private Provider<EmployeeManagementViewModel> employeeManagementViewModelProvider;

    private Provider<LoginViewModel> loginViewModelProvider;

    private Provider<OrderDetailViewModel> orderDetailViewModelProvider;

    private Provider<ProductManagementViewModel> productManagementViewModelProvider;

    private Provider<ReportViewModel> reportViewModelProvider;

    private Provider<ServiceManagementViewModel> serviceManagementViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.addServicesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.allOrdersViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.clientManagementViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.createOrderViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.employeeHomeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.employeeManagementViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.loginViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.orderDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.productManagementViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.reportViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.serviceManagementViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
    }

    @Override
    public Map<String, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(11).put("com.rfpiscinas.serviceorder.ui.viewmodel.AddServicesViewModel", ((Provider) addServicesViewModelProvider)).put("com.rfpiscinas.serviceorder.ui.viewmodel.AllOrdersViewModel", ((Provider) allOrdersViewModelProvider)).put("com.rfpiscinas.serviceorder.ui.viewmodel.ClientManagementViewModel", ((Provider) clientManagementViewModelProvider)).put("com.rfpiscinas.serviceorder.ui.viewmodel.CreateOrderViewModel", ((Provider) createOrderViewModelProvider)).put("com.rfpiscinas.serviceorder.ui.viewmodel.EmployeeHomeViewModel", ((Provider) employeeHomeViewModelProvider)).put("com.rfpiscinas.serviceorder.ui.viewmodel.EmployeeManagementViewModel", ((Provider) employeeManagementViewModelProvider)).put("com.rfpiscinas.serviceorder.ui.viewmodel.LoginViewModel", ((Provider) loginViewModelProvider)).put("com.rfpiscinas.serviceorder.ui.screens.employee.OrderDetailViewModel", ((Provider) orderDetailViewModelProvider)).put("com.rfpiscinas.serviceorder.ui.viewmodel.ProductManagementViewModel", ((Provider) productManagementViewModelProvider)).put("com.rfpiscinas.serviceorder.ui.viewmodel.ReportViewModel", ((Provider) reportViewModelProvider)).put("com.rfpiscinas.serviceorder.ui.viewmodel.ServiceManagementViewModel", ((Provider) serviceManagementViewModelProvider)).build();
    }

    @Override
    public Map<String, Object> getHiltViewModelAssistedMap() {
      return Collections.<String, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.rfpiscinas.serviceorder.ui.viewmodel.AddServicesViewModel 
          return (T) new AddServicesViewModel(singletonCImpl.clientRepositoryProvider.get(), singletonCImpl.serviceRepositoryProvider.get(), singletonCImpl.productRepositoryProvider.get(), singletonCImpl.serviceOrderRepositoryProvider.get());

          case 1: // com.rfpiscinas.serviceorder.ui.viewmodel.AllOrdersViewModel 
          return (T) new AllOrdersViewModel(singletonCImpl.serviceOrderRepositoryProvider.get());

          case 2: // com.rfpiscinas.serviceorder.ui.viewmodel.ClientManagementViewModel 
          return (T) new ClientManagementViewModel(singletonCImpl.clientRepositoryProvider.get());

          case 3: // com.rfpiscinas.serviceorder.ui.viewmodel.CreateOrderViewModel 
          return (T) new CreateOrderViewModel(singletonCImpl.serviceOrderRepositoryProvider.get(), singletonCImpl.clientRepositoryProvider.get(), singletonCImpl.serviceRepositoryProvider.get(), singletonCImpl.productRepositoryProvider.get());

          case 4: // com.rfpiscinas.serviceorder.ui.viewmodel.EmployeeHomeViewModel 
          return (T) new EmployeeHomeViewModel(singletonCImpl.serviceOrderRepositoryProvider.get());

          case 5: // com.rfpiscinas.serviceorder.ui.viewmodel.EmployeeManagementViewModel 
          return (T) new EmployeeManagementViewModel(singletonCImpl.userRepositoryProvider.get());

          case 6: // com.rfpiscinas.serviceorder.ui.viewmodel.LoginViewModel 
          return (T) new LoginViewModel(singletonCImpl.userRepositoryProvider.get());

          case 7: // com.rfpiscinas.serviceorder.ui.screens.employee.OrderDetailViewModel 
          return (T) new OrderDetailViewModel(singletonCImpl.serviceOrderRepositoryProvider.get());

          case 8: // com.rfpiscinas.serviceorder.ui.viewmodel.ProductManagementViewModel 
          return (T) new ProductManagementViewModel(singletonCImpl.productRepositoryProvider.get());

          case 9: // com.rfpiscinas.serviceorder.ui.viewmodel.ReportViewModel 
          return (T) new ReportViewModel(singletonCImpl.serviceOrderRepositoryProvider.get());

          case 10: // com.rfpiscinas.serviceorder.ui.viewmodel.ServiceManagementViewModel 
          return (T) new ServiceManagementViewModel(singletonCImpl.serviceRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends RFPiscinasApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends RFPiscinasApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends RFPiscinasApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AppDatabase> provideDatabaseProvider;

    private Provider<ClientRepository> clientRepositoryProvider;

    private Provider<ServiceRepository> serviceRepositoryProvider;

    private Provider<ProductRepository> productRepositoryProvider;

    private Provider<ServiceOrderRepository> serviceOrderRepositoryProvider;

    private Provider<UserRepository> userRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private ClientDao clientDao() {
      return DatabaseModule_ProvideClientDaoFactory.provideClientDao(provideDatabaseProvider.get());
    }

    private ServiceDao serviceDao() {
      return DatabaseModule_ProvideServiceDaoFactory.provideServiceDao(provideDatabaseProvider.get());
    }

    private ProductDao productDao() {
      return DatabaseModule_ProvideProductDaoFactory.provideProductDao(provideDatabaseProvider.get());
    }

    private ServiceOrderDao serviceOrderDao() {
      return DatabaseModule_ProvideServiceOrderDaoFactory.provideServiceOrderDao(provideDatabaseProvider.get());
    }

    private UserDao userDao() {
      return DatabaseModule_ProvideUserDaoFactory.provideUserDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 1));
      this.clientRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ClientRepository>(singletonCImpl, 0));
      this.serviceRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ServiceRepository>(singletonCImpl, 2));
      this.productRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ProductRepository>(singletonCImpl, 3));
      this.serviceOrderRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ServiceOrderRepository>(singletonCImpl, 4));
      this.userRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<UserRepository>(singletonCImpl, 5));
    }

    @Override
    public void injectRFPiscinasApplication(RFPiscinasApplication rFPiscinasApplication) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.rfpiscinas.serviceorder.data.repository.ClientRepository 
          return (T) new ClientRepository(singletonCImpl.clientDao());

          case 1: // com.rfpiscinas.serviceorder.data.local.AppDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.rfpiscinas.serviceorder.data.repository.ServiceRepository 
          return (T) new ServiceRepository(singletonCImpl.serviceDao());

          case 3: // com.rfpiscinas.serviceorder.data.repository.ProductRepository 
          return (T) new ProductRepository(singletonCImpl.productDao());

          case 4: // com.rfpiscinas.serviceorder.data.repository.ServiceOrderRepository 
          return (T) new ServiceOrderRepository(singletonCImpl.serviceOrderDao());

          case 5: // com.rfpiscinas.serviceorder.data.repository.UserRepository 
          return (T) new UserRepository(singletonCImpl.userDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
