create or alter procedure LAB4_2
as
declare variable BOOK integer;
declare variable ORD integer;
declare variable AUT integer;
declare variable U integer;
declare variable GIFT_PRICE float;
BEGIN
 for select user_id
      from orders where status=0  group by user_id
      into :u
      do
      begin
select first 1 books.au_id from books,orders,orders_entre 
where orders.order_id=orders_entre.order_id and books.book_id=orders_entre.book_id   and user_id=:u 
group by books.au_id  order by count(books.au_id)   desc
             into :aut;
             select first 1 book_id, price from books where au_id=:aut order by price into :book,:gift_price;
             select first 1 order_id from orders where user_id=:u order by order_date_create into :ord;
             insert into orders_entre values (:ord,:book, 14,:gift_price)  ;
      end
END
