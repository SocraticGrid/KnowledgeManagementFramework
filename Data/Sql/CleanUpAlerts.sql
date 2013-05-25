-- =========================================================
-- This script will remove all related records for a ticket
-- with the given id.
-- =========================================================
set @id := 10;

delete from alertcontact where at_ticketId in (
select at_ticketId from alertticket where alertticket.AT_TicketID > @id
);

delete from alertstatus where at_ticketId in (
select at_ticketId from alertticket where alertticket.AT_TicketID > @id
);

delete from alertaction where at_ticketId in (
select at_ticketId from alertticket where alertticket.AT_TicketID > @id
);

delete from alertticket where alertticket.AT_TicketID > @id;

Commit;

