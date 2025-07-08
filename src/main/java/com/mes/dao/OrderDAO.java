package com.mes.dao;
import com.mes.bean.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
	
	//新增訂單
	//conn = 資料庫連線物件(外部插入)
	 public int insertOrder(Connection conn, int supplierId, String orderDate, String status, Double subTotal ) throws Exception{
		String sql = "INSERT INTO PurchaseOrder (supplier_id, order_date, order_status, sub_total) VALUES (?,?,?,?)";
		try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			//Statement.RETURN_GENERATED_KEYS 表示：執行後可以取得自動產生的主鍵（例如自動遞增的 order_id）
			stmt.setInt(1, supplierId);
			stmt.setString(2, orderDate);
			stmt.setString(3, status);
			stmt.setDouble(4, subTotal);
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();//從 stmt 取得執行後產生的主鍵值（例如 order_id）
			if(rs.next()) {
				return rs.getInt(1);//回傳orderID
			}else {
				throw new Exception("未取得訂單PK");
			}	
		}
		 
	 }
	 //新增訂單明細
	 public void insertOrderItems(Connection conn, int orderId, String[]materialIds, String[]quantities, String[] unitPrices)throws Exception {
		String sql = "INSERT INTO PurchaseOrderItem (order_id, material_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
				try(PreparedStatement stmt = conn.prepareStatement(sql)){
					for (int i = 0; i < materialIds.length; i++) {
					stmt.setInt(1,orderId);
					stmt.setInt(2, Integer.parseInt(materialIds[i]));
					stmt.setInt(3, Integer.parseInt(quantities[i]));
					stmt.setDouble(4, Double.parseDouble(unitPrices[i]));
					stmt.addBatch();//把當前這一筆設定好的資料，加入 批次處理清單，還沒真正執行。
					}
					stmt.executeBatch();
				}		
	}
	// 查詢訂單，包含供應商名稱
	 public List<Order> getAllOrdersWithItems(Connection conn) throws Exception {
	     String sql = "SELECT o.order_id, o.supplier_id, o.order_date, o.order_status, o.sub_total, " +
	                  "s.supplier_name AS supplierName " +
	                  "FROM PurchaseOrder o " +
	                  "JOIN Supplier s ON o.supplier_id = s.supplier_id " +
	                  "ORDER BY o.order_id DESC";
	     
	     List<Order> orderList = new ArrayList<>();
	     
	     try (PreparedStatement stmt = conn.prepareStatement(sql);
	          ResultSet rs = stmt.executeQuery()) {
	         
	         while (rs.next()) {
	             Order order = new Order();
	             order.setOrderId(rs.getInt("order_id"));
	             order.setSupplierId(rs.getInt("supplier_id"));
	             order.setOrderDate(rs.getString("order_date"));
	             order.setOrderStatus(rs.getString("order_status"));
	             order.setSubTotal(rs.getDouble("sub_total"));
	             order.setSupplierName(rs.getString("supplierName")); 
	             
	          // 查出該筆訂單的明細
	             List<OrderItem> items = getItemsByOrderId(conn, order.getOrderId());
	             order.setItemList(items);

	             orderList.add(order);
	         }
	     }
	     return orderList;
	 }
	 
	 //刪除訂單明細
	 public void deleteOrderItems(Connection conn, int orderId) throws Exception{
		 String sql = "DELETE FROM PurchaseOrderItem WHERE order_id = ?";
		 try (PreparedStatement stmt = conn.prepareStatement(sql)){
			 stmt.setInt(1, orderId);
			 stmt.executeUpdate();
		} 
	 }
	 
	 //刪除訂單主檔
	 public void deleteOrder(Connection conn, int orderId) throws Exception{
		 String sql = "DELETE FROM PurchaseOrder WHERE order_id = ?";
		 try (PreparedStatement stmt = conn.prepareStatement(sql)){
			 stmt.setInt(1, orderId);
			 stmt.executeUpdate();
		} 
	 }
	 
	 //查詢訂單主檔
	 public Order getOrderById(Connection conn, int orderId) throws Exception{
		 String sql = "SELECT * FROM PurchaseOrder WHERE order_id = ?";
		 try (PreparedStatement stmt = conn.prepareStatement(sql)){
			 stmt.setInt(1, orderId);
			 try(ResultSet rs = stmt.executeQuery()){
				 if(rs.next()) {
					 Order order = new Order();		   
					order.setOrderId(rs.getInt("order_id"));
			 		order.setSupplierId(rs.getInt("supplier_id"));
			 		order.setOrderDate(rs.getString("order_date"));
			        order.setOrderStatus(rs.getString("order_status"));
			        order.setSubTotal(rs.getDouble("sub_total"));
			        return order;
				 }else {
					 throw new Exception("找不到訂單資料 ID="+ orderId);
				 }
			 }
		 }		
	 }
	 //查詢某一筆訂單的訂單明細
	 public List<OrderItem> getItemsByOrderId(Connection conn, int orderId)throws Exception{
		 String sql = "SELECT i.order_item_id, i.order_id, i.material_id, i.quantity, i.unit_price, i.delivery_status,"
		 		+ "               m.material_name"
		 		+ "        FROM PurchaseOrderItem i"
		 		+ "        JOIN Material m ON i.material_id = m.material_id"
		 		+ "        WHERE i.order_id = ?";
		 List<OrderItem>itemList = new ArrayList<>();
		 try (PreparedStatement stmt = conn.prepareStatement(sql)){
			 stmt.setInt(1, orderId);
			 try(ResultSet rs = stmt.executeQuery()){
				 while (rs.next()) {
					 OrderItem item = new OrderItem();
					 item.setOrderItemId(rs.getInt("order_item_id"));
		                item.setOrderId(rs.getInt("order_id"));
		                item.setMaterialId(rs.getInt("material_id"));
		                item.setQuantity(rs.getInt("quantity"));
		                item.setUnitPrice(rs.getDouble("unit_price"));
		                item.setDeliveryStatus(rs.getString("delivery_status"));
		                item.setMaterialName(rs.getString("material_name"));		                
		                itemList.add(item);
				 }
			 }
		 }
		 return itemList;
	 }
	 
	 //更新訂單主檔
	 public void updateOrder(Connection conn, int orderId, int supplierId, String orderDate, String status, double subTotal) throws Exception{
		 String sql = "UPDATE PurchaseOrder SET supplier_id = ?, order_date = ?, order_status = ?, sub_total = ? WHERE order_id = ?";
		 try (PreparedStatement stmt = conn.prepareStatement(sql)) {
		        stmt.setInt(1, supplierId);
		        stmt.setString(2, orderDate);
		        stmt.setString(3, status);
		        stmt.setDouble(4, subTotal);
		        stmt.setInt(5, orderId);
		        stmt.executeUpdate();
		 }
	 }
	 
	 //更新訂單明細
	 public void updateOrderItems(Connection conn, int orderId, String[] materialIds, String[] quantities, String[] unitPrices) throws Exception {
		// 1. 刪除原有明細
		    String deleteSQL = "DELETE FROM PurchaseOrderItem WHERE order_id = ?";
		    try (PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
		        stmt.setInt(1, orderId);
		        stmt.executeUpdate();
		    }
		 // 2. 重新新增明細
		    String insertSQL = "INSERT INTO PurchaseOrderItem (order_id, material_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
		    try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
		        for (int i = 0; i < materialIds.length; i++) {
		            stmt.setInt(1, orderId);
		            stmt.setInt(2, Integer.parseInt(materialIds[i]));
		            stmt.setInt(3, Integer.parseInt(quantities[i]));
		            stmt.setDouble(4, Double.parseDouble(unitPrices[i]));
		            stmt.addBatch();
		        }
		        stmt.executeBatch();
		    }   
	 }
	 
	// 同時刪除訂單主檔與明細
	 public void deleteOrderWithItems(Connection conn, int orderId) throws Exception {
	     // 1. 刪除明細
	     deleteOrderItems(conn, orderId);

	     // 2. 再刪主檔
	     deleteOrder(conn, orderId);
	 }

}//end
