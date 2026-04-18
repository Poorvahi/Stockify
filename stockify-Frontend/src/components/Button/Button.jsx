import React from "react";

export default function Button({
  onClick,
  children,
  className = "",
  variant = "default",
  disabled = false,
  title,
}) {
  const baseStyle =
    "inline-flex items-center justify-center font-semibold rounded-lg transition-all duration-200 ease-in-out focus:outline-none disabled:opacity-50 disabled:cursor-not-allowed shadow-sm hover:shadow-md";

  const variants = {
    default: "bg-gray-200 text-gray-800 hover:bg-gray-300",
    add: "bg-indigo-600 text-white hover:bg-indigo-700 hover:-translate-y-0.5",
    upload: "bg-emerald-500 text-white hover:bg-emerald-600 hover:-translate-y-0.5",
    download: "bg-sky-500 text-white hover:bg-sky-600 hover:-translate-y-0.5",
    edit: "bg-amber-500 text-white hover:bg-amber-600",
    delete: "bg-rose-500 text-white hover:bg-rose-600",
    comment: "bg-teal-500 text-white hover:bg-teal-600",
    barcode: "bg-purple-600 text-white hover:bg-purple-700",
  };

  const selectedVariant = variants[variant] || variants.default;

  return (
    <button
      onClick={onClick}
      disabled={disabled}
      title={title}
      className={`${baseStyle} ${selectedVariant} px-4 py-2 ${className}`}
    >
      {children}
    </button>
  );
}