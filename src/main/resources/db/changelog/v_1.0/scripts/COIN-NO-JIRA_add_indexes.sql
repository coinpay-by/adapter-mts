CREATE INDEX idx_transfers_status_internal_status
    ON transfers (transaction_status, internal_status);

CREATE INDEX idx_transfers_status_created_at
    ON transfers (transaction_status, created_at);

CREATE INDEX idx_transfers_transaction_date
    ON transfers (transaction_date);
