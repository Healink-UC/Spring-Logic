-- Fix JSONB column casting issue
ALTER TABLE interacciones_chatbot 
ALTER COLUMN contexto_conversacion TYPE jsonb USING COALESCE(contexto_conversacion::jsonb, '{}'); 