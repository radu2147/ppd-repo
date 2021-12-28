package objectProtocol;

import domain.VanzareDTO;

public class VanzareSoldResponse implements UpdateResponse{
    private VanzareDTO VanzareDTO;

    public VanzareSoldResponse(VanzareDTO VanzareDTO) {
        this.VanzareDTO = VanzareDTO;
    }

    public VanzareDTO getVanzareDTO() {
        return VanzareDTO;
    }

    public void setVanzareDTO(VanzareDTO VanzareDTO) {
        this.VanzareDTO = VanzareDTO;
    }
}
