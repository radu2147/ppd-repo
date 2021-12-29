package objectProtocol;

import domain.VanzareDTO;

public class VanzareRequest implements Request{
    private final VanzareDTO VanzareDTO;

    public VanzareRequest(VanzareDTO VanzareDTO) {
        this.VanzareDTO = VanzareDTO;
    }

    public VanzareDTO getVanzareDTO() {
        return VanzareDTO;
    }

}
