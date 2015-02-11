create or alter procedure LAB4_1 (
    G_ID integer,
    START_PER date,
    END_PER date)
returns (
    AVG_COST float)
as
declare variable SM float;
declare variable ORD_SM float;
declare variable CNT integer;
declare variable ORD_CNT integer;
declare variable BOOK integer;
declare variable ORD integer;
declare variable COUNTER integer;
declare variable PRICE float;
BEGIN

 ord_sm=0;
 ord_cnt=0;
  for select book_id
      from books_genre where genre_id=: g_id
      into :book
  do
  begin

        for select orders_entre.order_id
            from orders_entre ,orders where orders_entre.book_id=:book
            and orders.order_id=orders_entre.order_id and orders.order_date_create between :start_per and :end_per
             group by orders_entre.order_id
            into :ord
        do
        begin



            sm=0;
            counter=0;
            for select book_cnt,book_price
                from orders_entre  where order_id=:ord
                into :cnt,price
            do
            begin
                sm=:sm+:cnt*:price;
                counter=:counter+1;
            end
            ord_sm=:ord_sm+:sm;
            ord_cnt=:ord_cnt+1;
        end
  end
  avg_cost=:ord_sm/ord_cnt;
END
