DELIMITER $$

CREATE TRIGGER trg_delivery_items_master_update
AFTER UPDATE ON delivery_items_master
FOR EACH ROW
BEGIN
    UPDATE delivery_items
    SET 
        batch_no = NEW.batch_no,
        description = NEW.description,
        exp_date = NEW.exp_date,
        hsn_code = NEW.hsn_code,
        mfg_date = NEW.mfg_date,
        name = NEW.name,
        order_no = NEW.order_no,
        product_code = NEW.product_code,
        quantity = NEW.quantity,
        serial_no = NEW.serial_no,
        unit = NEW.unit,
        challen_id = NEW.challen_id,
        last_modified_date = NEW.last_modified_date
    WHERE id = NEW.id;
END$$

DELIMITER ;
