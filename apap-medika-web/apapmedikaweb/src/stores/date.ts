// src/utils/date.ts

/**
 * Memformat objek Date atau string tanggal ke format dd-MM-yyyy.
 * @param date - Objek Date atau string yang dapat di-parse menjadi Date.
 * @returns String tanggal dalam format dd-MM-yyyy.
 */
export function formatDateToDDMMYYYY(date: Date | string): string {
    const d = typeof date === 'string' ? new Date(date) : date;
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0'); // Bulan dimulai dari 0
    const year = d.getFullYear();
    return `${day}-${month}-${year}`;
  }
  