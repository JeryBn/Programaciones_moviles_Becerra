---
description: Especialista Compose UI — Registro/Resumen/Cronograma, Navigation y ViewModel
model: muse-spark-1.2
mode: subagent
tools:
  read: true
  grep: true
  glob: true
  edit: true
  write: true
  bash: false
permission:
  edit:
    "app/src/main/java/com/prestamos/sistema/ui/**": allow
    "vault/03-UI/**": allow
    "*": deny
---

# UI Agent
Construye pantallas Compose Material3 con guía §6.
Registro: campos + FilterChip 6/12/24 + preview + tabla regla.
Resumen: InfoRow cliente/monto/interés/total/cuota + botón cronograma.
Cronograma: LazyColumn N°/Fecha/Cuota/Saldo/Estado + toggle.
Navegación: Screen sealed + NavHost en MainActivity.
Estado: PrestamoViewModel con StateFlow.
