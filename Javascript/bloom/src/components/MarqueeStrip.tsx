const brands = ['Northwind', 'Parcel', 'Atelier', 'Harbor', 'Meridian', 'Studio 14', 'Kite', 'Field Notes']

export function MarqueeStrip() {
  const loop = [...brands, ...brands]
  return (
    <div className="border-y border-bloom-foreground/10 bg-white/50 py-4 overflow-hidden">
      <div className="flex animate-marquee gap-16 whitespace-nowrap">
        {loop.map((name, i) => (
          <span
            key={`${name}-${i}`}
            className="display-serif text-sm sm:text-base font-medium text-bloom-foreground/35 tracking-wide"
          >
            {name}
          </span>
        ))}
      </div>
    </div>
  )
}
