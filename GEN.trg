CREATE OR ALTER trigger gen for test2
active before insert position 0
AS
begin
   IF (new.test_id IS NULL) THEN
   NEW.test_id = GEN_ID(GEN, 1);

end
