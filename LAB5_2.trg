CREATE OR ALTER trigger lab5_2 for orders_entre
active after insert position 0
as
declare variable cost FLOAT;
declare variable d_cost FLOAT;
declare variable gift_id INT;
begin
    if(new.book_price!=0) then
    begin
        execute procedure lab5(new.order_id) returning_values  :cost;
        d_cost=new.book_price*new.book_cnt;
        if(((:cost+:d_cost)>300000)) then
        begin
            select first 1 book_id from top100  order by top100_id into :gift_id;
            insert into orders_entre values(new.order_id, :gift_id,1,0 );
        end
    end
end
