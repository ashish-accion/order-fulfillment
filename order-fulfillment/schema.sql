-- Table Creation Scripts (PostgreSQL)

CREATE TABLE orders (
  order_id       BIGSERIAL PRIMARY KEY,
  customer_id    BIGINT    NOT NULL,
  shipping_type  VARCHAR(10) NOT NULL CHECK (shipping_type IN ('STANDARD','EXPRESS','RAIL')),
  status         VARCHAR(10) NOT NULL DEFAULT 'OPEN' CHECK (status IN ('OPEN','PROCESSED','SHIPPED')),
  assigned_fc    INT,
  shipped_at     TIMESTAMP,
  version        BIGINT    NOT NULL DEFAULT 0,
  created_at     TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE order_status_log (
  log_id      BIGSERIAL PRIMARY KEY,
  order_id    BIGINT REFERENCES orders(order_id),
  old_status  VARCHAR(10),
  new_status  VARCHAR(10),
  changed_at  TIMESTAMP NOT NULL DEFAULT now(),
  notified    BOOLEAN NOT NULL DEFAULT FALSE
);
