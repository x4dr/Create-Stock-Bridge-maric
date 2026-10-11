package com.tom.stockbridge.ae;

/**
 * Unidirectional variant of the AE Stock Bridge: it makes the AE network's
 * inventory available to the Create stock frequency (Create can request AE
 * items), but exposes nothing back to AE and does not run the Create to AE
 * virtual-pattern path. Multiple providers may be placed on one frequency to
 * bind additional packagers, increasing dispatch throughput.
 */
public class AEStockProviderBlock extends AEStockBridgeBlock {

	public AEStockProviderBlock(Properties pProperties) {
		super(pProperties);
	}
}
