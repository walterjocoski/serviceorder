package com.rfpiscinas.serviceorder.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.rfpiscinas.serviceorder.data.local.Converters;
import com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderEntity;
import com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderItemEntity;
import com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderProductEntity;
import com.rfpiscinas.serviceorder.data.model.OrderStatus;
import com.rfpiscinas.serviceorder.data.model.UnitMeasure;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ServiceOrderDao_Impl implements ServiceOrderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ServiceOrderEntity> __insertionAdapterOfServiceOrderEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<ServiceOrderItemEntity> __insertionAdapterOfServiceOrderItemEntity;

  private final EntityInsertionAdapter<ServiceOrderProductEntity> __insertionAdapterOfServiceOrderProductEntity;

  private final EntityDeletionOrUpdateAdapter<ServiceOrderEntity> __deletionAdapterOfServiceOrderEntity;

  private final EntityDeletionOrUpdateAdapter<ServiceOrderEntity> __updateAdapterOfServiceOrderEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOrderStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteItemsByOrderId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteProductsByOrderId;

  public ServiceOrderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfServiceOrderEntity = new EntityInsertionAdapter<ServiceOrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `service_orders` (`id`,`clientId`,`clientName`,`clientAddress`,`employeeId`,`employeeName`,`status`,`startDateTime`,`endDateTime`,`synced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ServiceOrderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getClientId());
        statement.bindString(3, entity.getClientName());
        statement.bindString(4, entity.getClientAddress());
        statement.bindLong(5, entity.getEmployeeId());
        statement.bindString(6, entity.getEmployeeName());
        final String _tmp = __converters.fromOrderStatus(entity.getStatus());
        statement.bindString(7, _tmp);
        statement.bindString(8, entity.getStartDateTime());
        if (entity.getEndDateTime() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getEndDateTime());
        }
        final int _tmp_1 = entity.getSynced() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
      }
    };
    this.__insertionAdapterOfServiceOrderItemEntity = new EntityInsertionAdapter<ServiceOrderItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `service_order_items` (`id`,`serviceOrderId`,`serviceId`,`serviceName`,`serviceType`,`serviceUsesProducts`,`observations`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ServiceOrderItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getServiceOrderId());
        statement.bindLong(3, entity.getServiceId());
        statement.bindString(4, entity.getServiceName());
        statement.bindString(5, entity.getServiceType());
        final int _tmp = entity.getServiceUsesProducts() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindString(7, entity.getObservations());
      }
    };
    this.__insertionAdapterOfServiceOrderProductEntity = new EntityInsertionAdapter<ServiceOrderProductEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `service_order_products` (`id`,`serviceOrderItemId`,`productId`,`productName`,`productUnitMeasure`,`quantity`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ServiceOrderProductEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getServiceOrderItemId());
        statement.bindLong(3, entity.getProductId());
        statement.bindString(4, entity.getProductName());
        final String _tmp = __converters.fromUnitMeasure(entity.getProductUnitMeasure());
        statement.bindString(5, _tmp);
        statement.bindDouble(6, entity.getQuantity());
      }
    };
    this.__deletionAdapterOfServiceOrderEntity = new EntityDeletionOrUpdateAdapter<ServiceOrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `service_orders` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ServiceOrderEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfServiceOrderEntity = new EntityDeletionOrUpdateAdapter<ServiceOrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `service_orders` SET `id` = ?,`clientId` = ?,`clientName` = ?,`clientAddress` = ?,`employeeId` = ?,`employeeName` = ?,`status` = ?,`startDateTime` = ?,`endDateTime` = ?,`synced` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ServiceOrderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getClientId());
        statement.bindString(3, entity.getClientName());
        statement.bindString(4, entity.getClientAddress());
        statement.bindLong(5, entity.getEmployeeId());
        statement.bindString(6, entity.getEmployeeName());
        final String _tmp = __converters.fromOrderStatus(entity.getStatus());
        statement.bindString(7, _tmp);
        statement.bindString(8, entity.getStartDateTime());
        if (entity.getEndDateTime() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getEndDateTime());
        }
        final int _tmp_1 = entity.getSynced() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateOrderStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE service_orders SET status = ?, endDateTime = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteItemsByOrderId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM service_order_items WHERE serviceOrderId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteProductsByOrderId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM service_order_products WHERE serviceOrderItemId IN (SELECT id FROM service_order_items WHERE serviceOrderId = ?)";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrder(final ServiceOrderEntity order,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfServiceOrderEntity.insertAndReturnId(order);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertItem(final ServiceOrderItemEntity item,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfServiceOrderItemEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertProduct(final ServiceOrderProductEntity product,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfServiceOrderProductEntity.insertAndReturnId(product);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllOrders(final List<ServiceOrderEntity> orders,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfServiceOrderEntity.insert(orders);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllItems(final List<ServiceOrderItemEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfServiceOrderItemEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllProducts(final List<ServiceOrderProductEntity> products,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfServiceOrderProductEntity.insert(products);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOrder(final ServiceOrderEntity order,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfServiceOrderEntity.handle(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrder(final ServiceOrderEntity order,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfServiceOrderEntity.handle(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrderStatus(final long orderId, final String statusStr,
      final String endDateTime, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateOrderStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, statusStr);
        _argIndex = 2;
        if (endDateTime == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, endDateTime);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, orderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateOrderStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteItemsByOrderId(final long orderId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteItemsByOrderId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, orderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteItemsByOrderId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProductsByOrderId(final long orderId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteProductsByOrderId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, orderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteProductsByOrderId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ServiceOrderEntity>> getAll() {
    final String _sql = "SELECT * FROM service_orders ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"service_orders"}, new Callable<List<ServiceOrderEntity>>() {
      @Override
      @NonNull
      public List<ServiceOrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfClientName = CursorUtil.getColumnIndexOrThrow(_cursor, "clientName");
          final int _cursorIndexOfClientAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "clientAddress");
          final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
          final int _cursorIndexOfEmployeeName = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeName");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startDateTime");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endDateTime");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<ServiceOrderEntity> _result = new ArrayList<ServiceOrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ServiceOrderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpClientId;
            _tmpClientId = _cursor.getLong(_cursorIndexOfClientId);
            final String _tmpClientName;
            _tmpClientName = _cursor.getString(_cursorIndexOfClientName);
            final String _tmpClientAddress;
            _tmpClientAddress = _cursor.getString(_cursorIndexOfClientAddress);
            final long _tmpEmployeeId;
            _tmpEmployeeId = _cursor.getLong(_cursorIndexOfEmployeeId);
            final String _tmpEmployeeName;
            _tmpEmployeeName = _cursor.getString(_cursorIndexOfEmployeeName);
            final OrderStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toOrderStatus(_tmp);
            final String _tmpStartDateTime;
            _tmpStartDateTime = _cursor.getString(_cursorIndexOfStartDateTime);
            final String _tmpEndDateTime;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmpEndDateTime = null;
            } else {
              _tmpEndDateTime = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final boolean _tmpSynced;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_1 != 0;
            _item = new ServiceOrderEntity(_tmpId,_tmpClientId,_tmpClientName,_tmpClientAddress,_tmpEmployeeId,_tmpEmployeeName,_tmpStatus,_tmpStartDateTime,_tmpEndDateTime,_tmpSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ServiceOrderEntity>> getByClient(final long clientId) {
    final String _sql = "SELECT * FROM service_orders WHERE clientId = ? ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clientId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"service_orders"}, new Callable<List<ServiceOrderEntity>>() {
      @Override
      @NonNull
      public List<ServiceOrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfClientName = CursorUtil.getColumnIndexOrThrow(_cursor, "clientName");
          final int _cursorIndexOfClientAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "clientAddress");
          final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
          final int _cursorIndexOfEmployeeName = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeName");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startDateTime");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endDateTime");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<ServiceOrderEntity> _result = new ArrayList<ServiceOrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ServiceOrderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpClientId;
            _tmpClientId = _cursor.getLong(_cursorIndexOfClientId);
            final String _tmpClientName;
            _tmpClientName = _cursor.getString(_cursorIndexOfClientName);
            final String _tmpClientAddress;
            _tmpClientAddress = _cursor.getString(_cursorIndexOfClientAddress);
            final long _tmpEmployeeId;
            _tmpEmployeeId = _cursor.getLong(_cursorIndexOfEmployeeId);
            final String _tmpEmployeeName;
            _tmpEmployeeName = _cursor.getString(_cursorIndexOfEmployeeName);
            final OrderStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toOrderStatus(_tmp);
            final String _tmpStartDateTime;
            _tmpStartDateTime = _cursor.getString(_cursorIndexOfStartDateTime);
            final String _tmpEndDateTime;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmpEndDateTime = null;
            } else {
              _tmpEndDateTime = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final boolean _tmpSynced;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_1 != 0;
            _item = new ServiceOrderEntity(_tmpId,_tmpClientId,_tmpClientName,_tmpClientAddress,_tmpEmployeeId,_tmpEmployeeName,_tmpStatus,_tmpStartDateTime,_tmpEndDateTime,_tmpSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ServiceOrderEntity>> getByEmployee(final long employeeId) {
    final String _sql = "SELECT * FROM service_orders WHERE employeeId = ? ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, employeeId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"service_orders"}, new Callable<List<ServiceOrderEntity>>() {
      @Override
      @NonNull
      public List<ServiceOrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfClientName = CursorUtil.getColumnIndexOrThrow(_cursor, "clientName");
          final int _cursorIndexOfClientAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "clientAddress");
          final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
          final int _cursorIndexOfEmployeeName = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeName");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startDateTime");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endDateTime");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<ServiceOrderEntity> _result = new ArrayList<ServiceOrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ServiceOrderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpClientId;
            _tmpClientId = _cursor.getLong(_cursorIndexOfClientId);
            final String _tmpClientName;
            _tmpClientName = _cursor.getString(_cursorIndexOfClientName);
            final String _tmpClientAddress;
            _tmpClientAddress = _cursor.getString(_cursorIndexOfClientAddress);
            final long _tmpEmployeeId;
            _tmpEmployeeId = _cursor.getLong(_cursorIndexOfEmployeeId);
            final String _tmpEmployeeName;
            _tmpEmployeeName = _cursor.getString(_cursorIndexOfEmployeeName);
            final OrderStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toOrderStatus(_tmp);
            final String _tmpStartDateTime;
            _tmpStartDateTime = _cursor.getString(_cursorIndexOfStartDateTime);
            final String _tmpEndDateTime;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmpEndDateTime = null;
            } else {
              _tmpEndDateTime = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final boolean _tmpSynced;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_1 != 0;
            _item = new ServiceOrderEntity(_tmpId,_tmpClientId,_tmpClientName,_tmpClientAddress,_tmpEmployeeId,_tmpEmployeeName,_tmpStatus,_tmpStartDateTime,_tmpEndDateTime,_tmpSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getById(final long id, final Continuation<? super ServiceOrderEntity> $completion) {
    final String _sql = "SELECT * FROM service_orders WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ServiceOrderEntity>() {
      @Override
      @Nullable
      public ServiceOrderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfClientName = CursorUtil.getColumnIndexOrThrow(_cursor, "clientName");
          final int _cursorIndexOfClientAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "clientAddress");
          final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
          final int _cursorIndexOfEmployeeName = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeName");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startDateTime");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endDateTime");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final ServiceOrderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpClientId;
            _tmpClientId = _cursor.getLong(_cursorIndexOfClientId);
            final String _tmpClientName;
            _tmpClientName = _cursor.getString(_cursorIndexOfClientName);
            final String _tmpClientAddress;
            _tmpClientAddress = _cursor.getString(_cursorIndexOfClientAddress);
            final long _tmpEmployeeId;
            _tmpEmployeeId = _cursor.getLong(_cursorIndexOfEmployeeId);
            final String _tmpEmployeeName;
            _tmpEmployeeName = _cursor.getString(_cursorIndexOfEmployeeName);
            final OrderStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toOrderStatus(_tmp);
            final String _tmpStartDateTime;
            _tmpStartDateTime = _cursor.getString(_cursorIndexOfStartDateTime);
            final String _tmpEndDateTime;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmpEndDateTime = null;
            } else {
              _tmpEndDateTime = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final boolean _tmpSynced;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_1 != 0;
            _result = new ServiceOrderEntity(_tmpId,_tmpClientId,_tmpClientName,_tmpClientAddress,_tmpEmployeeId,_tmpEmployeeName,_tmpStatus,_tmpStartDateTime,_tmpEndDateTime,_tmpSynced);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getItemsByOrderId(final long orderId,
      final Continuation<? super List<ServiceOrderItemEntity>> $completion) {
    final String _sql = "SELECT * FROM service_order_items WHERE serviceOrderId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, orderId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ServiceOrderItemEntity>>() {
      @Override
      @NonNull
      public List<ServiceOrderItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfServiceOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "serviceOrderId");
          final int _cursorIndexOfServiceId = CursorUtil.getColumnIndexOrThrow(_cursor, "serviceId");
          final int _cursorIndexOfServiceName = CursorUtil.getColumnIndexOrThrow(_cursor, "serviceName");
          final int _cursorIndexOfServiceType = CursorUtil.getColumnIndexOrThrow(_cursor, "serviceType");
          final int _cursorIndexOfServiceUsesProducts = CursorUtil.getColumnIndexOrThrow(_cursor, "serviceUsesProducts");
          final int _cursorIndexOfObservations = CursorUtil.getColumnIndexOrThrow(_cursor, "observations");
          final List<ServiceOrderItemEntity> _result = new ArrayList<ServiceOrderItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ServiceOrderItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpServiceOrderId;
            _tmpServiceOrderId = _cursor.getLong(_cursorIndexOfServiceOrderId);
            final long _tmpServiceId;
            _tmpServiceId = _cursor.getLong(_cursorIndexOfServiceId);
            final String _tmpServiceName;
            _tmpServiceName = _cursor.getString(_cursorIndexOfServiceName);
            final String _tmpServiceType;
            _tmpServiceType = _cursor.getString(_cursorIndexOfServiceType);
            final boolean _tmpServiceUsesProducts;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfServiceUsesProducts);
            _tmpServiceUsesProducts = _tmp != 0;
            final String _tmpObservations;
            _tmpObservations = _cursor.getString(_cursorIndexOfObservations);
            _item = new ServiceOrderItemEntity(_tmpId,_tmpServiceOrderId,_tmpServiceId,_tmpServiceName,_tmpServiceType,_tmpServiceUsesProducts,_tmpObservations);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getProductsByItemId(final long itemId,
      final Continuation<? super List<ServiceOrderProductEntity>> $completion) {
    final String _sql = "SELECT * FROM service_order_products WHERE serviceOrderItemId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, itemId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ServiceOrderProductEntity>>() {
      @Override
      @NonNull
      public List<ServiceOrderProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfServiceOrderItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "serviceOrderItemId");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
          final int _cursorIndexOfProductUnitMeasure = CursorUtil.getColumnIndexOrThrow(_cursor, "productUnitMeasure");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final List<ServiceOrderProductEntity> _result = new ArrayList<ServiceOrderProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ServiceOrderProductEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpServiceOrderItemId;
            _tmpServiceOrderItemId = _cursor.getLong(_cursorIndexOfServiceOrderItemId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final String _tmpProductName;
            _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            final UnitMeasure _tmpProductUnitMeasure;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfProductUnitMeasure);
            _tmpProductUnitMeasure = __converters.toUnitMeasure(_tmp);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            _item = new ServiceOrderProductEntity(_tmpId,_tmpServiceOrderItemId,_tmpProductId,_tmpProductName,_tmpProductUnitMeasure,_tmpQuantity);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<String>> getDistinctEmployeeNames() {
    final String _sql = "SELECT DISTINCT employeeName FROM service_orders ORDER BY employeeName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"service_orders"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<String>> getDistinctClientNames() {
    final String _sql = "SELECT DISTINCT clientName FROM service_orders ORDER BY clientName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"service_orders"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
