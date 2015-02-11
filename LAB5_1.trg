CREATE OR ALTER trigger lab5_1 for orders
active before delete position 0
as
declare variable stat integer;
begin
stat=old.status;
if(stat!=0) then exception wrong_state_del;
end
