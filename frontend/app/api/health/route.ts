export async function GET() {
  return new Response(JSON.stringify({ ok: true, service: 'frontend' }), {
    headers: { 'Content-Type': 'application/json' },
  });
}
