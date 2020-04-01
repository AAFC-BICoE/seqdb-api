SELECT status, process, id, lastModified FROM public.DatabaseReadiness WHERE application = '$APPLICATION' ORDER BY lastModified DESC LIMIT 1;
