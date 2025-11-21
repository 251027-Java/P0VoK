-- Fix the rating constraint to enforce 0-5 scale with half-star increments
-- Run this SQL script in your PostgreSQL database

-- Drop the old constraint
ALTER TABLE reviews DROP CONSTRAINT IF EXISTS rating_range;

-- Add the new constraint that enforces 0-5 scale with half-star increments only
ALTER TABLE reviews ADD CONSTRAINT rating_range 
    CHECK (rating >= 0 AND rating <= 5 AND (rating * 2)::integer = rating * 2);

