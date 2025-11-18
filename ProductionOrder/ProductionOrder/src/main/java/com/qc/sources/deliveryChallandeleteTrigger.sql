DELIMITER $$

CREATE TRIGGER trg_delivery_challan_master_delete
AFTER DELETE ON delivery_challan_master
FOR EACH ROW
BEGIN
    DELETE FROM delivery_challan WHERE id = OLD.id;
END$$

DELIMITER ;
