DELIMITER $$

CREATE TRIGGER trg_delivery_challan_master_update
AFTER UPDATE ON delivery_challan_master
FOR EACH ROW
BEGIN
    UPDATE delivery_challan
    SET name = NEW.name,
        descriptions = NEW.descriptions,
        status = NEW.status,
        last_modified_date = NEW.last_modified_date
    WHERE id = NEW.id;
END$$

DELIMITER ;
