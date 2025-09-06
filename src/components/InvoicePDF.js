import { Document, Page, Text, View, StyleSheet, Font } from "@react-pdf/renderer";

// Đăng ký font Times New Roman
Font.register({
  family: "NotoSans",
  fonts: [
    { src: "/fonts/NotoSans-Regular.ttf", fontWeight: "normal" },
    { src: "/fonts/NotoSans-Bold.ttf", fontWeight: "bold" },
    { src: "/fonts/NotoSans-Italic.ttf", fontStyle: "italic" },
    { src: "/fonts/NotoSans-BoldItalic.ttf", fontWeight: "bold", fontStyle: "italic" },
  ],
});

const styles = StyleSheet.create({
  page: { padding: 30, fontSize: 12, fontFamily: "NotoSans" },
  header: { fontSize: 18, marginBottom: 20, textAlign: "center", fontWeight: "bold" },
  section: { marginBottom: 15 },
  label: { fontWeight: "bold", marginBottom: 5 },

  table: {
    display: "table",
    width: "auto",
    borderStyle: "solid",
    borderWidth: 1,
    borderRightWidth: 0,
    borderBottomWidth: 0,
    marginTop: 5,
  },
  tableRow: { flexDirection: "row" },

  // Cho bảng thông tin user (2 cột)
  tableCol2: {
    width: "50%",
    borderStyle: "solid",
    borderWidth: 1,
    borderLeftWidth: 0,
    borderTopWidth: 0,
    padding: 5,
  },

  // Cho bảng invoice info (6 cột)
  tableCol6: {
    width: "16.6%", // 100% / 6 cột
    borderStyle: "solid",
    borderWidth: 1,
    borderLeftWidth: 0,
    borderTopWidth: 0,
    padding: 5,
  },

  // Cho bảng detail (4 cột)
  tableCol4: {
    width: "25%", // 100% / 4 cột
    borderStyle: "solid",
    borderWidth: 1,
    borderLeftWidth: 0,
    borderTopWidth: 0,
    padding: 5,
  },

  footer: {
    position: "absolute",
    bottom: 30,
    left: 0,
    right: 0,
  },
  footerText: {
    textAlign: "center",
    fontSize: 10,
    fontStyle: "italic",
  },
});

const InvoicePDF = ({ invoice, user }) => {
  return (
    <Document>
      <Page size="A4" style={styles.page}>
        <Text style={styles.header}>SUNHOUSE APARTMENT</Text>
        <Text style={styles.header}>INVOICE - {invoice.id}</Text>

        {/* User Info */}
        <View style={styles.section}>
          <Text style={styles.label}>Customer Information</Text>
          <View style={styles.table}>
            <View style={styles.tableRow}>
              <Text style={styles.tableCol2}>Full Name</Text>
              <Text style={styles.tableCol2}>{user?.fullName || "N/A"}</Text>
            </View>
            <View style={styles.tableRow}>
              <Text style={styles.tableCol2}>Email</Text>
              <Text style={styles.tableCol2}>{user?.email || "N/A"}</Text>
            </View>
            <View style={styles.tableRow}>
              <Text style={styles.tableCol2}>Phone</Text>
              <Text style={styles.tableCol2}>{user?.phone || "N/A"}</Text>
            </View>
          </View>
        </View>

        {/* Invoice Info */}
        <View style={styles.section}>
          <Text style={styles.label}>Invoice Information</Text>
          <View style={styles.table}>
            {/* Header row */}
            <View style={styles.tableRow}>
              <Text style={styles.tableCol6}>Invoice ID</Text>
              <Text style={styles.tableCol6}>Total Amount</Text>
              <Text style={styles.tableCol6}>Payment Method</Text>
              <Text style={styles.tableCol6}>Issued Date</Text>
              <Text style={styles.tableCol6}>Due Date</Text>
              <Text style={styles.tableCol6}>Status</Text>
            </View>
            {/* Value row */}
            <View style={styles.tableRow}>
              <Text style={styles.tableCol6}>{invoice.id}</Text>
              <Text style={styles.tableCol6}>{invoice.totalAmount} VND</Text>
              <Text style={styles.tableCol6}>{invoice.paymentMethod}</Text>
              <Text style={styles.tableCol6}>{invoice.issuedDate}</Text>
              <Text style={styles.tableCol6}>{invoice.dueDate}</Text>
              <Text style={styles.tableCol6}>{invoice.status}</Text>
            </View>
          </View>
        </View>

        {/* Invoice Details */}
        <View style={styles.section}>
          <Text style={styles.label}>Invoice Details</Text>
          <View style={styles.table}>
            {/* Header */}
            <View style={styles.tableRow}>
              <Text style={styles.tableCol4}>Fee ID</Text>
              <Text style={styles.tableCol4}>Fee Name</Text>
              <Text style={styles.tableCol4}>Amount</Text>
              <Text style={styles.tableCol4}>Note</Text>
            </View>
            {/* Data */}
            {invoice.details?.map((d, idx) => (
              <View style={styles.tableRow} key={idx}>
                <Text style={styles.tableCol4}>{d.feeId}</Text>
                <Text style={styles.tableCol4}>{d.feeName}</Text>
                <Text style={styles.tableCol4}>{d.amount} VND</Text>
                <Text style={styles.tableCol4}>{d.note}</Text>
              </View>
            ))}
          </View>
        </View>

        {/* Footer */}
        <View style={styles.footer}>
          <Text style={styles.footerText}>Invoice provided by Sunhouse Apartment</Text>
        </View>
      </Page>
    </Document>
  );
};

export default InvoicePDF;
