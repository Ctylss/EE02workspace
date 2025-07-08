package com.mes.dao;
import com.mes.bean.*;
import java.util.List;
import org.hibernate.Session;

public class OrderDAO {
	
	//新增訂單
	 public int insertOrder(Session session, int supplierId, String orderDate, String status, Double subTotal ) {
		 Supplier supplier = session.find(Supplier.class, supplierId);
		Order order = new Order();
			order.setSupplier(supplier);
			order.setOrderDate(orderDate);
			order.setOrderStatus(status);
			order.setSubTotal(subTotal);
			session.persist(order);
			return order.getOrderId();
	 }

	 //新增訂單明細
	 public void insertOrderItems(Session session, int orderId, String[]materialIds, String[]quantities, String[] unitPrices)throws Exception {
			for (int i = 0; i < materialIds.length; i++) {
				OrderItem orderItem = new OrderItem();
				orderItem.setOrderId(orderId);
				orderItem.setMaterialId(Integer.parseInt(materialIds[i]));
				orderItem.setQuantity(Integer.parseInt(quantities[i]));
				orderItem.setUnitPrice(Double.parseDouble(unitPrices[i]));
				orderItem.setDeliveryStatus("出貨中");
				session.persist(orderItem);	
			}
	}
	// 先查詢訂單，包含供應商名稱
	 public List<Order> getAllOrdersWithItems(Session session) throws Exception {
	     	String sql = "SELECT o FROM Order o JOIN FETCH o.supplier ORDER BY o.orderId DESC";
	    	List<Order> orderList = session.createQuery(sql,Order.class).getResultList();

	// 再查訂單明細
			for(Order order : orderList){
			List<OrderItem> orderItems = getItemsByOrderId(session, order.getOrderId());
			order.setItemList(orderItems);
		}
		return orderList;
	 }
	 
	 //刪除訂單明細
	 public void deleteOrderItems(Session session, int orderId) throws Exception{
		 String sql = "DELETE FROM OrderItem WHERE orderId = :orderId";
		 	session.createQuery(sql).setParameter("orderId", orderId).executeUpdate();
	 }
	 
	 //刪除訂單主檔
	 public void deleteOrder(Session session, int orderId) throws Exception{
		 String sql = "DELETE FROM Order WHERE orderId = :orderId";
		 session.createQuery(sql).setParameter("orderId", orderId).executeUpdate();

	 }
	 
	 //查詢訂單主檔
	 public Order getOrderById(Session session, int orderId) throws Exception{
		 Order order = session.find(Order.class, orderId);
			if(order  == null){
				throw new Exception("找不到訂單編號 ID="+orderId);
			}
		 return order;
	}

	 //查詢某一筆訂單的訂單明細
	 public List<OrderItem> getItemsByOrderId(Session session, int orderId)throws Exception{
		 String sql = "SELECT i FROM OrderItem i JOIN FETCH i.material WHERE i.orderId = :orderId";
    	return session.createQuery(sql, OrderItem.class)
		.setParameter("orderId", orderId)
		.getResultList();
	 }
	 
	 //更新訂單主檔
	 public void updateOrder(Session session, int orderId, int supplierId, String orderDate, String status, double subTotal) throws Exception{
		 Order order = session.find(Order.class, orderId);
		Supplier supplier = session.find(Supplier.class, supplierId);
		 if(order != null){
		        order.setSupplier(supplier);
		        order.setOrderDate(orderDate);
		        order.setOrderStatus(status);
		        order.setSubTotal(subTotal);
				
		        session.merge(order);
		}else{
			throw new Exception("找不到訂單 ID:"+orderId);
		}

	}
	 
	 //更新訂單明細
	 public void updateOrderItems(Session session, int orderId, String[] materialIds, String[] quantities, String[] unitPrices) throws Exception {
		// 1. 刪除原有明細
		    String deleteSQL = "DELETE FROM OrderItem WHERE orderId = :orderId";
			session.createQuery(deleteSQL).setParameter("orderId", orderId).executeUpdate();
		    
		        for (int i = 0; i < materialIds.length; i++) {
					OrderItem orderItem = new OrderItem();
		            orderItem.setOrderId(orderId);
		            orderItem.setMaterialId(Integer.parseInt(materialIds[i]));
		            orderItem.setQuantity(Integer.parseInt(quantities[i]));
		            orderItem.setUnitPrice(Double.parseDouble(unitPrices[i]));
					orderItem.setDeliveryStatus("出貨中");
		            session.persist(orderItem);
		        }
	  }   
	 
	// 同時刪除訂單主檔與明細
	 public void deleteOrderWithItems(Session session, int orderId) throws Exception {
		Order order = session.find(Order.class, orderId);
		if(order != null){
			List<OrderItem> items = getItemsByOrderId(session, orderId);
			for(OrderItem item : items){
				session.remove(item);
			}
			session.remove(order);
		}
	 }
	
}//end
