CREATE OR ALTER trigger delete_orders for orders
active before delete position 0
AS
begin
  delete from orders_entre  where  orders_entre.order_id=old.order_id;
end
