package io.github.ravidshachar.craftana;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.common.TextFormat;

@SuppressWarnings("serial")
public class MCExporter extends HttpServlet {
	private CollectorRegistry registry;
	static final Gauge playerAmount = Gauge.build().name("craftana_online_players").help("Current online craftana players").register();
	static final Gauge clockAmount = Gauge.build().name("craftana_clocks").help("Amount of craftana clocks").register();
	static final Gauge graphAmount = Gauge.build().name("craftana_graphs").help("Amount of craftana graphs").register();
	static final Gauge histogramAmount = Gauge.build().name("craftana_histograms").help("Amount of craftana histograms").register();
	
	public MCExporter() {
	    this(CollectorRegistry.defaultRegistry);
	  }
	
	public MCExporter(CollectorRegistry registry) {
	    this.registry = registry;
	  }
	
	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
	    resp.setStatus(HttpServletResponse.SC_OK);
	    resp.setContentType(TextFormat.CONTENT_TYPE_004);

	    Writer writer = new BufferedWriter(resp.getWriter());
	    try {
	      TextFormat.write004(writer, registry.filteredMetricFamilySamples(parse(req)));
	      writer.flush();
	    } finally {
	      writer.close();
	    }
	}
	
	private Set<String> parse(HttpServletRequest req) {
	    String[] includedParam = req.getParameterValues("name[]");
	    if (includedParam == null) {
	      return Collections.emptySet();
	    } else {
	      return new HashSet<String>(Arrays.asList(includedParam));
	    }
	}
	
	public static void reset() {
		playerAmount.set(0);
		clockAmount.set(0);
		graphAmount.set(0);
		histogramAmount.set(0);
	}
}
