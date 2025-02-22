-- Create customers table
CREATE TABLE
  customers (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    second_last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    credit_line_amount DECIMAL(10, 2) NOT NULL,
    available_credit_line_amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL
  );

-- Create loans table
CREATE TABLE
  loans (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customers (id),
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL
  );

-- Create installments table
CREATE TABLE
  installments (
    id UUID PRIMARY KEY,
    loan_id UUID NOT NULL REFERENCES loans (id),
    amount DECIMAL(10, 2) NOT NULL,
    scheduled_payment_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL
  );

-- Create indexes
CREATE INDEX idx_customers_created_at ON customers (created_at);

CREATE INDEX idx_loans_customer_id ON loans (customer_id);

CREATE INDEX idx_loans_created_at ON loans (created_at);

CREATE INDEX idx_installments_loan_id ON installments (loan_id);

CREATE INDEX idx_installments_scheduled_payment_date ON installments (scheduled_payment_date);