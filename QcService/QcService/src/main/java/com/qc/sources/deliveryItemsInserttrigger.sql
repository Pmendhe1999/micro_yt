DELIMITER $$

CREATE TRIGGER trg_delivery_items_master_insert
AFTER INSERT ON delivery_items_master
FOR EACH ROW
BEGIN
    INSERT INTO delivery_items (
        id,
        batch_no,
        description,
        exp_date,
        hsn_code,
        mfg_date,
        name,
        order_no,
        product_code,
        quantity,
        serial_no,
        unit,
        qualitative_check_passed_qty,
        qualitative_check_failed_qty,
        quantitative_check_passed_qty,
        quantitative_check_failed_qty,
        challen_id,
        created_date,
        last_modified_date
    )
    VALUES (
        NEW.id,
        NEW.batch_no,
        NEW.description,
        NEW.exp_date,
        NEW.hsn_code,
        NEW.mfg_date,
        NEW.name,
        NEW.order_no,
        NEW.product_code,
        NEW.quantity,
        NEW.serial_no,
        NEW.unit,
        0,                          -- default qualitative passed qty
        0,                          -- default qualitative failed qty
        0,                          -- default quantitative passed qty
        0,                          -- default quantitative failed qty
        NEW.challen_id,
        NEW.created_date,
        NEW.last_modified_date
    );
END$$

DELIMITER ;
