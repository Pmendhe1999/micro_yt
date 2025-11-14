DELIMITER $$

CREATE TRIGGER trg_delivery_items_master_delete
AFTER DELETE ON delivery_items_master
FOR EACH ROW
BEGIN
    DELETE FROM delivery_items
    WHERE id = OLD.id;
END$$

DELIMITER ;
