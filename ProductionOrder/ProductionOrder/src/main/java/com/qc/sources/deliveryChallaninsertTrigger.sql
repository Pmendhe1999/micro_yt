DELIMITER $$

CREATE TRIGGER trg_delivery_challan_master_insert
AFTER INSERT ON delivery_challan_master
FOR EACH ROW
BEGIN
    INSERT INTO delivery_challan (id, name, descriptions, status, created_date, last_modified_date)
    VALUES (NEW.id, NEW.name, NEW.descriptions, NEW.status, NEW.created_date, NEW.last_modified_date);
END$$

DELIMITER ;
