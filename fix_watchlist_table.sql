-- Fix the watchlist table to make watchlistID auto-increment
-- Run this SQL script in your PostgreSQL database

-- Step 1: Create a sequence for watchlistID
CREATE SEQUENCE IF NOT EXISTS watchlist_watchlistid_seq;

-- Step 2: Set the sequence to start from the current max watchlistID + 1
SELECT setval('watchlist_watchlistid_seq', COALESCE((SELECT MAX("watchlistID") FROM watchlist), 0) + 1, false);

-- Step 3: Alter the watchlistID column to use the sequence as default
ALTER TABLE watchlist ALTER COLUMN "watchlistID" SET DEFAULT nextval('watchlist_watchlistid_seq');

-- Step 4: Make sure the sequence is owned by the column (optional but recommended)
ALTER SEQUENCE watchlist_watchlistid_seq OWNED BY watchlist."watchlistID";

