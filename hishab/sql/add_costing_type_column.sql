-- Add costing_type column to cost table
ALTER TABLE cost ADD COLUMN costing_type VARCHAR(20) NOT NULL DEFAULT 'QUANTITY';

-- Update existing records to have a default costing type
UPDATE cost SET costing_type = 'QUANTITY' WHERE costing_type IS NULL;
